import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Card } from '../models/card';

@Injectable({
  providedIn: 'root'
})
export class CardService {
  private apiUrl = `${window.location.origin}/api/cards`;

  constructor(private http: HttpClient) {
  }

  getAllCards(): Observable<Card[]> {
    return this.http.get<Card[]>(this.apiUrl);
  }

  getCardById(id: number): Observable<Card> {
    return this.http.get<Card>(`${this.apiUrl}/get/${id}`);
  }

  addCard(card: Card): Observable<Card> {
    return this.http.post<Card>(this.apiUrl, card);
  }

  deleteCard(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  updateCard(card: Card): Observable<Card> {
    return this.http.put<Card>(`${this.apiUrl}/put/${card.cardNumber}`, card);
  }

  uploadFile(formData: FormData): Observable<Card[]> {
    return this.http.post<Card[]>(`${this.apiUrl}/import`, formData);
  }

  getStats(): Observable<any> {
    return this.http.get(`${this.apiUrl}/stats`);
  }

  getCollectionValue(): Observable<any> {
    return this.http.get(`${this.apiUrl}/values`);
  }
}
