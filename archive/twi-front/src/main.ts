import { createApp } from 'vue'
import { createPinia } from 'pinia'
import * as ElementPlusIconsVue from '@element-plus/icons-vue'
import router from './route'

import './style.css'
import App from './App.vue'


const pinia = createPinia()
const app = createApp(App);
app.use(pinia).use(router).mount('#app')

for (const [key, component] of Object.entries(ElementPlusIconsVue)) {
    app.component(key, component)
}
