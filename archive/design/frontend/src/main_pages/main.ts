import { createApp } from 'vue'
import './style.css'
import App from './App.vue'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import { createPinia } from 'pinia'
import { createWebHashHistory, createRouter } from 'vue-router' 

import MainPage from '../components/mainPage.vue'
import ProductDetailPage from '../components/productDetailPage.vue'
import ShippingPage from '../components/shippingPage.vue'
import UserPage from '../components/userPage.vue'
import UserProfileEditPage from '../components/userProfileEditPage.vue'
import AdministratorPageVue from '../components/administratorPage.vue'
import manageProductPageVue from '../components/manageProductPage.vue'
import orderListPageAgain from '../components/orderListPageAgain.vue'
import searchPage from '../components/searchPage.vue'

const routes = [
    { path: '/', component: MainPage },
    { path: '/home', component: MainPage },
    { path: '/user', component: UserPage },
    { path: '/user/:id', component: UserPage, props: true},
    { path: '/edit-profile', component: UserProfileEditPage },
    { path: '/shipping/:id', component: ShippingPage, props: true },
    { path: '/product/:id', component: ProductDetailPage, props: true},
    { path: '/order', component: orderListPageAgain},
    { path: '/order/:id', component: orderListPageAgain, props: true},
    { path: '/test', component: ProductDetailPage },
    { path: '/a/admin', component: AdministratorPageVue},
    { path: '/a/manage', component: manageProductPageVue, props: true },
    { path: '/a/manage/:id', component: manageProductPageVue, props: true },
    { path: '/search', component: searchPage, props: true },
    { path: '/search/:keyword', component: searchPage, props: true }
    
  ]

  const router = createRouter({
    // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: createWebHashHistory(),
    routes, // `routes: routes` 的缩写
  })


const pinia = createPinia()
const app = createApp(App)

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
  app.component(key, component)
}

app.use(pinia)
app.use(router)
app.mount('#app')
