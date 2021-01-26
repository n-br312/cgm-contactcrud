import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import './vendor';
import { CgmContactcrudSharedModule } from 'app/shared/shared.module';
import { CgmContactcrudCoreModule } from 'app/core/core.module';
import { CgmContactcrudAppRoutingModule } from './app-routing.module';
import { CgmContactcrudHomeModule } from './home/home.module';
import { CgmContactcrudEntityModule } from './entities/entity.module';
// jhipster-needle-angular-add-module-import JHipster will add new module here
import { MainComponent } from './layouts/main/main.component';
import { NavbarComponent } from './layouts/navbar/navbar.component';
import { FooterComponent } from './layouts/footer/footer.component';
import { PageRibbonComponent } from './layouts/profiles/page-ribbon.component';
import { ErrorComponent } from './layouts/error/error.component';

@NgModule({
  imports: [
    BrowserModule,
    CgmContactcrudSharedModule,
    CgmContactcrudCoreModule,
    CgmContactcrudHomeModule,
    // jhipster-needle-angular-add-module JHipster will add new module here
    CgmContactcrudEntityModule,
    CgmContactcrudAppRoutingModule,
  ],
  declarations: [MainComponent, NavbarComponent, ErrorComponent, PageRibbonComponent, FooterComponent],
  bootstrap: [MainComponent],
})
export class CgmContactcrudAppModule {}
