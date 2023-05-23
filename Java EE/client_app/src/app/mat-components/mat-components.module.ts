import { NgModule } from '@angular/core';
// added imports
import { MatButtonModule } from '@angular/material/button';
import { MatCardModule } from '@angular/material/card';
import { MatMenuModule } from '@angular/material/menu';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatIconModule } from '@angular/material/icon';
import { MatInputModule } from '@angular/material/input';
import { MatListModule } from '@angular/material/list';
import { MatFormFieldModule } from '@angular/material/form-field';
import {MatSelectModule} from '@angular/material/select';
import {MatPaginatorModule} from '@angular/material/paginator';

const MaterialComponents = [MatButtonModule,
  MatCardModule,
  MatFormFieldModule,
  MatMenuModule,
  MatIconModule,
  MatInputModule,
  MatListModule,
  MatToolbarModule,
  MatSelectModule,
  MatPaginatorModule
];
@NgModule({
  imports: [MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatMenuModule,
    MatIconModule,
    MatInputModule,
    MatListModule,
    MatToolbarModule,
    MatSelectModule,
    MatPaginatorModule
  ],
  exports: [MatButtonModule,
    MatCardModule,
    MatFormFieldModule,
    MatMenuModule,
    MatInputModule,
    MatIconModule,
    MatListModule,
    MatToolbarModule,
    MatSelectModule,
    MatPaginatorModule
  ]
})
export class MatComponentsModule { }
