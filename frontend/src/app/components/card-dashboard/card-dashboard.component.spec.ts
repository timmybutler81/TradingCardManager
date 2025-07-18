import {ComponentFixture, TestBed} from '@angular/core/testing';

import {CardDashboardComponent} from './card-dashboard.component';

describe('CardDashboardComponent', () => {
  let component: CardDashboardComponent;
  let fixture: ComponentFixture<CardDashboardComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CardDashboardComponent]
    })
      .compileComponents();

    fixture = TestBed.createComponent(CardDashboardComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
