import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacilityList } from './facility-list';

describe('FacilityList', () => {
  let component: FacilityList;
  let fixture: ComponentFixture<FacilityList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FacilityList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacilityList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
