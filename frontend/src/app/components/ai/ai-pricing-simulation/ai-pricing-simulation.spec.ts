import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AiPricingSimulation } from './ai-pricing-simulation';

describe('AiPricingSimulation', () => {
  let component: AiPricingSimulation;
  let fixture: ComponentFixture<AiPricingSimulation>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AiPricingSimulation]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AiPricingSimulation);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
