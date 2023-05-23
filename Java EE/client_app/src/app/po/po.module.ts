import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ReactiveFormsModule } from '@angular/forms';
import { MatComponentsModule } from '../mat-components/mat-components.module';
import { PoGeneratorComponent } from './generator/po-generator.component';
import { PoViewerComponent } from './viewer/po-viewer.component';


@NgModule({
  declarations: [PoGeneratorComponent, PoViewerComponent],
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatComponentsModule
  ]
})
export class PoModule { }
