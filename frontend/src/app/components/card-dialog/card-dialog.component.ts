/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * July 5, 2025
 * card-dialog.component.ts
 * This component displays a list of fields to add or edit for the card selected.
 */
import {Component, Inject} from '@angular/core';
import {CommonModule} from '@angular/common';
import {FormBuilder, FormGroup, ReactiveFormsModule, Validators} from '@angular/forms';
import {MatFormFieldModule} from '@angular/material/form-field';
import {MatInputModule} from '@angular/material/input';
import {MatButtonModule} from '@angular/material/button';
import {MatSelectModule} from '@angular/material/select';
import {MatCheckboxModule} from '@angular/material/checkbox';
import {MAT_DIALOG_DATA, MatDialogRef, MatDialogTitle} from '@angular/material/dialog';

import {Card} from '../../models/card';
import {CardService} from '../../services/card.service';
import {MatSnackBar} from '@angular/material/snack-bar';

@Component({
  selector: 'app-card-dialog',
  standalone: true,
  templateUrl: './card-dialog.component.html',
  styleUrls: ['./card-dialog.component.css'],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
    MatSelectModule,
    MatCheckboxModule,
    MatDialogTitle
  ]
})
export class CardDialogComponent {
  cardForm: FormGroup;
  isEditMode: boolean;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: Card | null,
    private dialogRef: MatDialogRef<CardDialogComponent>,
    private fb: FormBuilder,
    private cardService: CardService,
    private snackBar: MatSnackBar
  ) {
    this.isEditMode = !!data;

    this.cardForm = this.fb.group({
      cardNumber: [data?.cardNumber || '', Validators.required],
      cardName: [data?.cardName || '', Validators.required],
      cardGame: [data?.cardGame || '', Validators.required],
      rarity: [data?.rarity || '', Validators.required],
      datePurchased: [data?.datePurchased || '', Validators.required],
      dateSetPublished: [data?.dateSetPublished || '', Validators.required],
      purchasePrice: [data?.purchasePrice || '', Validators.required],
      foiled: [data?.foiled || false]
    });
  }

  /**
   * Method: onSubmit
   * Purpose: Logic to submit a card to the backend
   * Parameters: none
   * Returns: void
   */
  onSubmit(): void {
    this.cardForm.markAllAsTouched();
    if (this.cardForm.invalid) return;

    const card: Card = this.cardForm.value;

    if (this.isEditMode) {
      this.cardService.updateCard(card).subscribe({
        next: (updated) => {
          this.snackBar.open('Card updated successfully!', 'Close', {duration: 3000});
          this.dialogRef.close(updated);
        },
        error: (err) =>
          this.snackBar.open(
            this.extractBackendMessage(err, 'Update failed'),
            'Close',
            {duration: 4000}
          ),
      });
    } else {
      this.cardService.addCard(card).subscribe({
        next: (added) => {
          this.snackBar.open('Card added successfully!', 'Close', {duration: 3000});
          this.dialogRef.close(added);
        },
        error: (err) =>
          this.snackBar.open(
            this.extractBackendMessage(err, 'Add failed'),
            'Close',
            {duration: 4000}
          ),
      });
    }
  }

  cancel(): void {
    this.dialogRef.close();
  }

  /**
   * Method: extractBackendMessage
   * Purpose: Handles returning errors from the backend to the front end
   * Parameters: error and fallback
   * Returns: string
   */
  private extractBackendMessage(err: any, fallback: string): string {
    if (err?.error?.error) return `${fallback}: ${err.error.error}`;
    if (err?.error?.details) return `${fallback}: ${err.error.details}`;
    if (err?.message) return `${fallback}: ${err.message}`;
    return fallback;
  }
}
