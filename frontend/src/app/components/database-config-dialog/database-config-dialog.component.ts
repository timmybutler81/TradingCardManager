import { Component } from '@angular/core';
import { Router } from '@angular/router';
import { MatDialogRef } from '@angular/material/dialog';
import { FormsModule } from '@angular/forms';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatDialogModule } from '@angular/material/dialog';
import { HttpClient } from '@angular/common/http';
import { CommonModule } from '@angular/common';
import { MatButtonModule } from '@angular/material/button';

@Component({
  selector: 'app-database-config-dialog',
  standalone: true,
  templateUrl: './database-config-dialog.component.html',
  styleUrls: ['./database-config-dialog.component.css'],
  imports: [
    CommonModule,
    FormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule
  ]
})
export class DatabaseConfigDialogComponent {
  dbConfig = {
    host: 'localhost',
    port: '3306',
    databaseName: 'trading_card_manager_db',
    dbUser: '',
    dbPass: ''
  };

  errorMessage: string | null = null;

  constructor(
    private dialogRef: MatDialogRef<DatabaseConfigDialogComponent>,
    private http: HttpClient,
    private router: Router
  ) {
  }

  connectToDatabase(): void {
    const payload = {
      host: this.dbConfig.host,
      port: this.dbConfig.port,
      databaseName: this.dbConfig.databaseName,
      username: this.dbConfig.dbUser,
      password: this.dbConfig.dbPass
    };

    this.http.post('/api/configure-database', payload, {responseType: 'text'}).subscribe({
      next: () => {
        this.dialogRef.close(true);
        this.router.navigate(['']); // Navigate to the default path
      },
      error: (error) => {
        console.error('Connection failed:', error);
        let message = 'Connection failed';
        if (error.error && typeof error.error === 'string') {
          message = error.error;
        } else if (error.error && error.error.message) {
          message = error.error.message;
        }
        this.errorMessage = message;
      }
    });
  }
}
