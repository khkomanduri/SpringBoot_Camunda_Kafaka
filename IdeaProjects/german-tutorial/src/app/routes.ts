import  {Routes} from '@angular/router';
import {HomeComponent} from './home-component/home-component';
import {CatalogComponent} from './catalog-component/catalog-component';
import {TopicsList} from './topics-list/topics-list';
import {MenuList} from './menu-list/menu-list';
const routeConfig: Routes = [
  {
    path: '',
    component: HomeComponent,
    title: 'Home page',
  },
  {
    path: 'home',
    component: HomeComponent,
    title: 'Home page',
  },
  {
    path: 'catalog',
    component: CatalogComponent,
    title: 'Catalog details',
  },

  {
    path: 'menu-list',
    component: MenuList,
    title: 'Menu',
  },
  {
    path: 'topics-list',
    component: TopicsList,
    title: 'Topics',
  },
];
export default routeConfig;
