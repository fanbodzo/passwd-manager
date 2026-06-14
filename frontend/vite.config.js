import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  server: {
    port: 3000, // <--- wymusza start frontendu na porcie 3000
    proxy: {
      '/api': {
        target: 'http://127.0.0.1:9000', // <--- uderza do Twojego Gatewaya
        changeOrigin: true,
        secure: false,
      },
    },
  },
})