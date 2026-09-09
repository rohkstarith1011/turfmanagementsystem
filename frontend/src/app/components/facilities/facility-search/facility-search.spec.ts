import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FacilitySearch } from './facility-search';

describe('FacilitySearch', () => {
  let component: FacilitySearch;
  let fixture: ComponentFixture<FacilitySearch>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [FacilitySearch]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FacilitySearch);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
