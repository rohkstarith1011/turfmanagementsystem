import { ComponentFixture, TestBed } from '@angular/core/testing';

import { SlotManagement } from './slot-management';

describe('SlotManagement', () => {
  let component: SlotManagement;
  let fixture: ComponentFixture<SlotManagement>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SlotManagement]
    })
    .compileComponents();

    fixture = TestBed.createComponent(SlotManagement);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
