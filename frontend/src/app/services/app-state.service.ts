import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class AppStateService {
  private dbReady = new BehaviorSubject<boolean>(false);
  dbReady$ = this.dbReady.asObservable();

  setDbReady(isReady: boolean): void {
    this.dbReady.next(isReady);
  }
}
