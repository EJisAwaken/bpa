import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FactureLecteurComponent } from './facture-lecteur.component';

describe('FactureLecteurComponent', () => {
  let component: FactureLecteurComponent;
  let fixture: ComponentFixture<FactureLecteurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FactureLecteurComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FactureLecteurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
