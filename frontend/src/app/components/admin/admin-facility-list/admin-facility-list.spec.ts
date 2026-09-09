import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminFacilityList } from './admin-facility-list';

describe('AdminFacilityList', () => {
  let component: AdminFacilityList;
  let fixture: ComponentFixture<AdminFacilityList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AdminFacilityList]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminFacilityList);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
