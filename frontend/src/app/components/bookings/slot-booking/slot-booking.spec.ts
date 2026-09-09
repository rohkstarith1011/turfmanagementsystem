import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SlotBooking } from './slot-booking';

describe('SlotBooking', () => {
  let component: SlotBooking;
  let fixture: ComponentFixture<SlotBooking>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SlotBooking]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SlotBooking);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
