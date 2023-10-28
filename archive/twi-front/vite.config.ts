import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import AutoImport from 'unplugin-auto-import/vite'
import Components from 'unplugin-vue-components/vite'
import { ElementPlusResolver } from 'unplugin-vue-components/resolvers'
import IconsResolver from 'unplugin-icons/resolver'
import Icons from 'unplugin-icons/vite'
import path from "path";
import copy from 'rollup-plugin-copy'

const pathSrc = path.resolve(__dirname, './src')
// https://vitejs.dev/config/
export default defineConfig({
  server: {
    origin: 'http://localhost:3000'
  },
  resolve: {
    alias: {
      "@assets": path.join(__dirname, "./public"),
      "@": path.join(__dirname, "./src"),
    },
  },
  build: {
    assetsDir: 'src/assets',
    manifest: true,
    rollupOptions: {
      output: {
        // assetFileNames: 'assets/[name][extname]',
        // intro: 'const __base = import.meta.env.BASE_URL'
      },
      input: {
        entry: path.resolve(__dirname, 'src/login/index.html'),
        main: path.resolve(__dirname, 'index.html')
      }
    },
    assetsInlineLimit: 0
  },
  plugins: [
    vue(),
    copy({
      targets: [
        {
          src: 'src/assets/*', // 源文件夹路径
          dest: 'dist/src/assets' // 目标文件夹路径
        },
      ]
    }),
    AutoImport({
      // Auto import functions from Vue, e.g. ref, reactive, toRef...
      // 自动导入 Vue 相关函数，如：ref, reactive, toRef 等
      imports: ['vue'],

      // Auto import functions from Element Plus, e.g. ElMessage, ElMessageBox... (with style)
      // 自动导入 Element Plus 相关函数，如：ElMessage, ElMessageBox... (带样式)
      resolvers: [
        ElementPlusResolver(),

        // Auto import icon components
        // 自动导入图标组件
        IconsResolver({
          prefix: 'Icon',
        }),
      ],

      dts: path.resolve(pathSrc, 'auto-imports.d.ts'),
    }),

    Components({
      resolvers: [
        // Auto register icon components
        // 自动注册图标组件
        IconsResolver({
          enabledCollections: ['ep'],
        }),
        // Auto register Element Plus components
        // 自动导入 Element Plus 组件
        ElementPlusResolver(),
      ],

      dts: path.resolve(pathSrc, 'components.d.ts'),
    }),

    Icons({
      autoInstall: true,
    }),

  ],
})
