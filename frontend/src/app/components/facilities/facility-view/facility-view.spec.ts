import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacilityView } from './facility-view';

describe('FacilityView', () => {
  let component: FacilityView;
  let fixture: ComponentFixture<FacilityView>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FacilityView]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacilityView);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
