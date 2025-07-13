/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * July 5, 2025
 * card-display.component.ts
 * This component displays a list of trading cards and allows the user to select one to modify or delete.
 */
import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Card } from '../../models/card';
import { CardService } from '../../services/card.service';
import { MatDialog } from '@angular/material/dialog';
import { CardDialogComponent } from '../card-dialog/card-dialog.component';

import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule }   from '@angular/material/card';
import { MatIconModule }   from '@angular/material/icon';
import { MatDialogModule } from '@angular/material/dialog';
import { MatSnackBar } from '@angular/material/snack-bar';
import {CollectionValueDialogComponent} from '../collection-value-dialog/collection-value-dialog.component';
import {AppStateService} from '../../services/app-state.service';
import {Subscription} from 'rxjs';

@Component({
  selector: 'app-card-dashboard',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatCardModule,
    MatIconModule,
    MatDialogModule
  ],
  templateUrl: './card-dashboard.component.html',
  styleUrls: ['./card-dashboard.component.css']
})
export class CardDashboardComponent implements OnInit {

  private dbSub!: Subscription;
  cards: Card[] = [];
  displayedColumns: string[] = [
    'cardNumber', 'cardName', 'cardGame',
    'rarity', 'datePurchased', 'dateSetPublished', 'purchasePrice', 'foiled'
  ];

  selectedCard: Card | null = null;
  stats: any = {};
  collectionValue: any = {};

  constructor(
    private cardService: CardService,
    private dialog: MatDialog,
    private snackBar: MatSnackBar,
    private appState: AppStateService) {}

  ngOnInit(): void {
    this.dbSub = this.appState.dbReady$.subscribe(ready => {
      if (ready) {
        this.loadDashboardData();
      }
    })
  }

  ngOnDestry(): void {
    this.dbSub.unsubscribe();
  }

  loadDashboardData(): void {
    this.loadCards();
    this.loadStats();
    this.loadCollectionValue();
  }

  /**
   * Method: loadCards
   * Purpose: Loads the cards from the backend
   * Parameters: none
   * Returns: void
   */
  loadCards(): void {
    this.cardService.getAllCards()
      .subscribe(data => {
        this.cards = data;
        console.log(this.cards);
      });
  }

  /**
   * Method: loadStats
   * Purpose: Loads the stats from the backend
   * Parameters: none
   * Returns: void
   */
  loadStats(): void {
    this.cardService.getStats()
      .subscribe(data => this.stats = data);
  }

  /**
   * Method: loadCollectionValue
   * Purpose: Loads the collection values from backend
   * Parameters: none
   * Returns: void
   */
  loadCollectionValue(): void {
    this.cardService.getCollectionValue()
      .subscribe(data => this.collectionValue = data);
  }

  /**
   * Method: onRowClick
   * Purpose: Captures the card selected in the table
   * Parameters: Card
   * Returns: void
   */
  onRowClick(card: Card): void {
    this.selectedCard = card;
  }

  /**
   * Method: deleteSelectedCard
   * Purpose: Handles card deletion called by the delete card button
   * Parameters: none
   * Returns: void
   */
  deleteSelectedCard(): void {
    if (!this.selectedCard) return;

    if (confirm(`Delete card #${this.selectedCard.cardNumber}?`)) {
      this.cardService.deleteCard(this.selectedCard.cardNumber).subscribe({
        next: () => {
          this.snackBar.open('Card deleted successfully!', 'Close', { duration: 3000 });
          this.selectedCard = null;
          this.loadCards();
          this.loadStats();
          this.loadCollectionValue();
        },
        error: (err) => {
          this.snackBar.open(err.error?.error || 'Delete failed', 'Close', { duration: 3000 });
        }
      });
    }
  }

  /**
   * Method: openAddDialog
   * Purpose: opens the dialog box for entering new cards
   * Parameters: none
   * Returns: void
   */
  openAddDialog(): void {
    const dialogRef = this.dialog.open(CardDialogComponent, {
      width: '400px',
      data: null,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
          this.loadCards();
          this.loadStats();
        this.loadCollectionValue();
      }
    })
  }

  /**
   * Method: openModifyDialog
   * Purpose: opens and loads the card into the dialog box
   * Parameters: none
   * Returns: void
   */
  openModifyDialog(): void {
    if (!this.selectedCard) return;

    const dialogRef = this.dialog.open(CardDialogComponent, {
      width: '400px',
      data: this.selectedCard
    });

    dialogRef.afterClosed().subscribe((result: Card) => {
      if (result) {
        this.loadCards();
        this.loadStats();
        this.loadCollectionValue();
        this.selectedCard = result;
      }
    });
  }

  /**
   * Method: uploadFile
   * Purpose: Reads the uploaded text file and imports cards
   * Parameters:
   *   - event: Event - the file input event triggered by the user
   * Returns: void
   */
  uploadFile(event: Event): void {
    const input = event.target as HTMLInputElement;
    const file = input.files?.[0];
    if (!file) return;

    const formData = new FormData();
    formData.append('file', file);

    this.cardService.uploadFile(formData).subscribe({
      next: (imported) => {
        this.snackBar.open(`Imported ${imported.length} cards`, 'Close', { duration: 3000 });
        this.loadCards();
        this.loadStats();
      },
      error: (err) => {
        this.snackBar.open(err.error?.error || 'Import failed', 'Close', { duration: 3000 });
      }
    });
  }

  /**
   * Method: openStatsDialog
   * Purpose: Opens the collection value modal
   * Parameters: none
   * Returns: void
   */
  openStatsDialog(): void {
    this.cardService.getCollectionValue().subscribe(value => {
      this.dialog.open(CollectionValueDialogComponent, {
        data: {
          ownerValue: value.ownerValue,
          marketValue: value.marketValue
        },
        width: '400px'
      });
    });
  }
}
