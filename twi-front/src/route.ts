import { createRouter, createWebHashHistory } from 'vue-router'
import TimelineList from "./components/timelinePage/TimelineList.vue";
import UserHomePage from "./components/homePage/UserHomePage.vue";
import NewTweet from "./components/NewTweet.vue";
import SearchBar from "./components/searchPage/SearchBar.vue"
const routes = [
    {path: '/', component: TimelineList},
    {path: '/timeline', component: TimelineList},
    {path: '/home', component: UserHomePage},
    {path: '/write', component: NewTweet},
    {path: '/search', component: SearchBar}
]

const router = createRouter({
    // 4. 内部提供了 history 模式的实现。为了简单起见，我们在这里使用 hash 模式。
    history: createWebHashHistory(),
    routes: routes, // `routes: routes` 的缩写
  })

export default router;