import { createRouter, createWebHistory } from 'vue-router'
import Home from '@/views/Home.vue'
import Layout from "@/layout/Layout";

const routes = [
  {
    path: '/',
    name: 'Layout',
    component: Layout,
   redirect:'/home',
   children:[{
     path:'home',
     name:'Home',
     component:()=>import("@/views/Home"),
   },
     {
       path: 'graph',
       name: 'Graph',
       component:()=>import("@/views/Graph"),
     }
   ]


  },
  {
    path: '/Login',
    name: 'Login',
    // route level code-splitting
    // this generates a separate chunk (about.[hash].js) for this route
    // which is lazy-loaded when the route is visited.
    component: () => import(/* webpackChunkName: "about" */ '@/views/Login.vue')
  },
  // {
  //   path: '/Graph',
  //   name: 'Graph',
  //   component: () => import('@/views/Graph.vue')
  // }
]

const router = createRouter({
  history: createWebHistory(process.env.BASE_URL),
  routes
})

export default router
