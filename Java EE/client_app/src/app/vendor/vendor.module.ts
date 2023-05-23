import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { VendorListComponent } from './vendor-list.component';
import { VendorHomeComponent } from './vendor-home.component';
import {MatCardModule} from '@angular/material/card';
import {MatComponentsModule} from '../mat-components/mat-components.module';
import { VendorDetailComponent } from './vendor-detail.component';
import {ReactiveFormsModule} from '@angular/forms';
import {MatOptionModule} from '@angular/material/core';



@NgModule({
  declarations: [VendorListComponent, VendorHomeComponent, VendorDetailComponent],
  imports: [
    CommonModule,
    MatCardModule,
    MatComponentsModule,
    ReactiveFormsModule,
    MatOptionModule
  ]
})
export class VendorModule { }
