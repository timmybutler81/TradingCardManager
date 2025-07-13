export interface Card {
  id?: number;
  cardNumber: number;
  cardName: string;
  cardGame: string;
  rarity: 'COMMON' | 'UNCOMMON' | 'HERO' | 'LEGENDARY';
  datePurchased: string;
  dateSetPublished: string;
  purchasePrice: number;
  foiled: boolean;
}
