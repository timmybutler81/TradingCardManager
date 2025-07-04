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
    private snackBar: MatSnackBar) {}

  ngOnInit(): void {
    this.loadCards();
    this.loadStats();
    this.loadCollectionValue();
  }

  loadCards(): void {
    this.cardService.getAllCards()
      .subscribe(data => {
        this.cards = data;
        console.log(this.cards);
      });
  }

  loadStats(): void {
    this.cardService.getStats()
      .subscribe(data => this.stats = data);
  }

  loadCollectionValue(): void {
    this.cardService.getCollectionValue()
      .subscribe(data => this.collectionValue = data);
  }

  onRowClick(card: Card): void {
    this.selectedCard = card;
  }

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

  openAddDialog(): void {
    const dialogRef = this.dialog.open(CardDialogComponent, {
      width: '400px',
      data: null,
    });

    dialogRef.afterClosed().subscribe((result) => {
      if (result) {
          this.loadCards();
          this.loadStats();
      }
    })
  }

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

  showCollectionValue(): void {
    this.cardService.getCollectionValue().subscribe({
      next: (value) => {
        const message = `Owner Value: $${value.ownerValue} | Market Value: $${value.marketValue}`;
        this.snackBar.open(message, 'Close', { duration: 5000 });
      },
      error: (err) => {
        this.snackBar.open(err.error?.error || 'Failed to fetch values', 'Close', { duration: 3000 });
      }
    });
  }

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
