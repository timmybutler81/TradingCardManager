/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * July 13, 2025
 * app-state.service.ts
 * This service the state of the app for connection calls to the data source
 */
import {Injectable} from '@angular/core';
import {BehaviorSubject} from 'rxjs';

@Injectable({providedIn: 'root'})
export class AppStateService {
  private dbReady = new BehaviorSubject<boolean>(false);
  dbReady$ = this.dbReady.asObservable();

  setDbReady(isReady: boolean): void {
    this.dbReady.next(isReady);
  }
}
