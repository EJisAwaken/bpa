import { ComponentFixture, TestBed } from '@angular/core/testing';

import { DetailNotifComponent } from './detail-notif.component';

describe('DetailNotifComponent', () => {
  let component: DetailNotifComponent;
  let fixture: ComponentFixture<DetailNotifComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ DetailNotifComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(DetailNotifComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
