import { Component, Inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CommonModule } from '@angular/common';
import { MatDialogModule } from '@angular/material/dialog';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-collection-value-dialog',
  standalone: true,
  templateUrl: './collection-value-dialog.component.html',
  styleUrls: ['./collection-value-dialog.component.css'],
  imports: [
    CommonModule,
    MatDialogModule,
    MatButtonModule
  ]
})
export class CollectionValueDialogComponent {
  ownerValue: number;
  marketValue: number;

  constructor(
    @Inject(MAT_DIALOG_DATA) public data: { ownerValue: number; marketValue: number },
    private dialogRef: MatDialogRef<CollectionValueDialogComponent>
  ) {
    this.ownerValue = Math.round(data.ownerValue / 100) * 100;
    this.marketValue = Math.round(data.marketValue / 100) * 100;
  }

  close(): void {
    this.dialogRef.close();
  }
}
