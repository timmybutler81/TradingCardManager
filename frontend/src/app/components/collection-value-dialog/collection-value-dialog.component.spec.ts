import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CollectionValueDialogComponent } from './collection-value-dialog.component';

describe('CollectionValueDialogComponent', () => {
  let component: CollectionValueDialogComponent;
  let fixture: ComponentFixture<CollectionValueDialogComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [CollectionValueDialogComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CollectionValueDialogComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
