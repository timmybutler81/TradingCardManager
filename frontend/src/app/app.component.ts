import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { MatDialog } from '@angular/material/dialog';
import { DatabaseConfigDialogComponent } from './components/database-config-dialog/database-config-dialog.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, DatabaseConfigDialogComponent],
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {
  title = 'Trading Card Manager';

  constructor(private dialog: MatDialog) {}

  ngOnInit(): void {
    this.dialog.open(DatabaseConfigDialogComponent, {
      disableClose: true,
      width: '400px'
    });
  }
}
