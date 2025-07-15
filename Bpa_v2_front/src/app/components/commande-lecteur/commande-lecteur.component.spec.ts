import { ComponentFixture, TestBed } from '@angular/core/testing';

import { CommandeLecteurComponent } from './commande-lecteur.component';

describe('CommandeLecteurComponent', () => {
  let component: CommandeLecteurComponent;
  let fixture: ComponentFixture<CommandeLecteurComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ CommandeLecteurComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(CommandeLecteurComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
