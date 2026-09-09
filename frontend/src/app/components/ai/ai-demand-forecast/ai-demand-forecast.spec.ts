import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AiDemandForecast } from './ai-demand-forecast';

describe('AiDemandForecast', () => {
  let component: AiDemandForecast;
  let fixture: ComponentFixture<AiDemandForecast>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [AiDemandForecast]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AiDemandForecast);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
