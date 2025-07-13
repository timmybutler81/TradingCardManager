/**
 * Timothy Butler
 * CEN 3024 - Software Development 1
 * July 5, 2025
 * card.service.ts
 * This service handles HTTP requests to the backend API for crud operations
 */
import {Injectable} from '@angular/core';
import {HttpClient} from '@angular/common/http';
import {Observable} from 'rxjs';
import {Card} from '../models/card';

@Injectable({
  providedIn: 'root'
})
export class CardService {
  private apiUrl = `${window.location.origin}/api/cards`;

  constructor(private http: HttpClient) {
  }

  /**
   * Method: getAllCards
   * Purpose: Retrieves the list of cards from the backend API.
   * Parameters: none
   * Returns: Card array
   */
  getAllCards(): Observable<Card[]> {
    return this.http.get<Card[]>(this.apiUrl);
  }

  /**
   * Method: getCardById
   * Purpose: Retrieves a card from the backend API.
   * Parameters: id
   * Returns: Card
   */
  getCardById(id: number): Observable<Card> {
    return this.http.get<Card>(`${this.apiUrl}/get/${id}`);
  }

  /**
   * Method: addCard
   * Purpose: Calls backend API to add card.
   * Parameters: Card
   * Returns: Card
   */
  addCard(card: Card): Observable<Card> {
    return this.http.post<Card>(this.apiUrl, card);
  }

  /**
   * Method: deleteCard
   * Purpose: Calls backend API to delete card
   * Parameters: id
   * Returns: Card
   */
  deleteCard(id: number): Observable<void> {
    return this.http.delete<void>(`${this.apiUrl}/delete/${id}`);
  }

  /**
   * Method: updateCard
   * Purpose: Calls backend API to update a card
   * Parameters: Card
   * Returns: Card
   */
  updateCard(card: Card): Observable<Card> {
    return this.http.put<Card>(`${this.apiUrl}/put/${card.cardNumber}`, card);
  }

  /**
   * Method: uploadFile
   * Purpose: Calls the backend API to submit a file
   * Parameters: FormData (front end file select)
   * Returns: Card array
   */
  uploadFile(formData: FormData): Observable<Card[]> {
    return this.http.post<Card[]>(`${this.apiUrl}/import`, formData);
  }

  /**
   * Method: getStats
   * Purpose: Calls the backend API to get card stats
   * Parameters: none
   * Returns: array
   */
  getStats(): Observable<any> {
    return this.http.get(`${this.apiUrl}/stats`);
  }

  /**
   * Method: getCollectionValue
   * Purpose: RCalls the backend API to get collection value
   * Parameters: none
   * Returns: array
   */
  getCollectionValue(): Observable<any> {
    return this.http.get(`${this.apiUrl}/values`);
  }
}
