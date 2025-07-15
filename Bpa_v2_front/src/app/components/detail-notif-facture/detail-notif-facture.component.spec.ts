import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailNotifFactureComponent } from './detail-notif-facture.component';

describe('DetailNotifFactureComponent', () => {
  let component: DetailNotifFactureComponent;
  let fixture: ComponentFixture<DetailNotifFactureComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailNotifFactureComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailNotifFactureComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
