/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
import { useEffect, useMemo } from 'react'
import { Routes, Route } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import { ThemeProvider, createTheme, CssBaseline } from '@mui/material'
import { LocalizationProvider } from '@mui/x-date-pickers'
import { AdapterDateFns } from '@mui/x-date-pickers/AdapterDateFns'

import { isRTL } from './i18n'
import Layout from './components/Layout'
import HomePage from './pages/HomePage'
import FnolFormPage from './pages/FnolFormPage'
import SuccessPage from './pages/SuccessPage'
import TrackingPage from './pages/TrackingPage'

function App() {
  const { i18n } = useTranslation()
  const currentLang = i18n.language
  const rtl = isRTL(currentLang)

  // Update document direction based on language
  useEffect(() => {
    document.documentElement.dir = rtl ? 'rtl' : 'ltr'
    document.documentElement.lang = currentLang
  }, [rtl, currentLang])

  // Premium theme with modern color palette
  const theme = useMemo(
    () =>
      createTheme({
        direction: rtl ? 'rtl' : 'ltr',
        palette: {
          mode: 'light',
          primary: {
            main: '#0ea5e9',
            light: '#38bdf8',
            dark: '#0284c7',
            contrastText: '#ffffff',
          },
          secondary: {
            main: '#6366f1',
            light: '#818cf8',
            dark: '#4f46e5',
            contrastText: '#ffffff',
          },
          success: {
            main: '#22c55e',
            light: '#86efac',
            dark: '#16a34a',
          },
          warning: {
            main: '#f59e0b',
            light: '#fcd34d',
            dark: '#d97706',
          },
          error: {
            main: '#ef4444',
            light: '#fca5a5',
            dark: '#dc2626',
          },
          background: {
            default: '#f8fafc',
            paper: '#ffffff',
          },
          text: {
            primary: '#1e293b',
            secondary: '#64748b',
          },
          divider: '#e2e8f0',
        },
        typography: {
          fontFamily: rtl
            ? '"Noto Sans Arabic", "Tahoma", "Arial", sans-serif'
            : '"Inter", -apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, sans-serif',
          h1: { fontWeight: 700, fontSize: '2.5rem', lineHeight: 1.2 },
          h2: { fontWeight: 700, fontSize: '2rem', lineHeight: 1.3 },
          h3: { fontWeight: 600, fontSize: '1.75rem', lineHeight: 1.3 },
          h4: { fontWeight: 600, fontSize: '1.5rem', lineHeight: 1.4 },
          h5: { fontWeight: 600, fontSize: '1.25rem', lineHeight: 1.4 },
          h6: { fontWeight: 600, fontSize: '1rem', lineHeight: 1.5 },
          body1: { fontSize: '1rem', lineHeight: 1.6 },
          body2: { fontSize: '0.875rem', lineHeight: 1.6 },
          button: { fontWeight: 600, textTransform: 'none' },
        },
        shape: {
          borderRadius: 12,
        },
        components: {
          MuiTextField: {
            defaultProps: { variant: 'outlined', fullWidth: true },
            styleOverrides: {
              root: {
                '& .MuiOutlinedInput-root': {
                  borderRadius: 12,
                  backgroundColor: '#ffffff',
                  transition: 'all 0.2s ease',
                  '&:hover': { backgroundColor: '#f8fafc' },
                  '&.Mui-focused': {
                    backgroundColor: '#ffffff',
                    boxShadow: '0 0 0 3px rgba(14, 165, 233, 0.1)',
                  },
                  '& fieldset': { borderColor: '#e2e8f0', transition: 'border-color 0.2s ease' },
                  '&:hover fieldset': { borderColor: '#cbd5e1' },
                  '&.Mui-focused fieldset': { borderColor: '#0ea5e9', borderWidth: '2px' },
                },
                '& .MuiInputLabel-root': {
                  color: '#64748b',
                  '&.Mui-focused': { color: '#0ea5e9' },
                },
              },
            },
          },
          MuiSelect: {
            styleOverrides: {
              root: { borderRadius: 12 },
            },
          },
          MuiButton: {
            defaultProps: { disableElevation: true },
            styleOverrides: {
              root: {
                textTransform: 'none',
                borderRadius: 12,
                padding: '12px 24px',
                fontSize: '0.9375rem',
                fontWeight: 600,
                transition: 'all 0.2s ease',
              },
              contained: {
                boxShadow: '0 4px 14px 0 rgba(14, 165, 233, 0.25)',
                '&:hover': {
                  transform: 'translateY(-2px)',
                  boxShadow: '0 6px 20px 0 rgba(14, 165, 233, 0.35)',
                },
              },
              containedPrimary: {
                background: 'linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%)',
                '&:hover': { background: 'linear-gradient(135deg, #38bdf8 0%, #0ea5e9 100%)' },
              },
              containedSecondary: {
                background: 'linear-gradient(135deg, #6366f1 0%, #4f46e5 100%)',
                boxShadow: '0 4px 14px 0 rgba(99, 102, 241, 0.25)',
                '&:hover': {
                  background: 'linear-gradient(135deg, #818cf8 0%, #6366f1 100%)',
                  boxShadow: '0 6px 20px 0 rgba(99, 102, 241, 0.35)',
                },
              },
              outlined: {
                borderWidth: '2px',
                '&:hover': { borderWidth: '2px', backgroundColor: 'rgba(14, 165, 233, 0.04)' },
              },
              sizeLarge: { padding: '14px 32px', fontSize: '1rem' },
            },
          },
          MuiCard: {
            styleOverrides: {
              root: {
                borderRadius: 16,
                boxShadow: '0 1px 3px 0 rgb(0 0 0 / 0.1), 0 1px 2px -1px rgb(0 0 0 / 0.1)',
                transition: 'all 0.25s ease',
                border: '1px solid #f1f5f9',
                '&:hover': { boxShadow: '0 10px 40px -10px rgb(0 0 0 / 0.12)' },
              },
            },
          },
          MuiCardContent: {
            styleOverrides: {
              root: { padding: '24px', '&:last-child': { paddingBottom: '24px' } },
            },
          },
          MuiPaper: {
            styleOverrides: {
              root: { backgroundImage: 'none' },
              rounded: { borderRadius: 16 },
            },
          },
          MuiAppBar: {
            styleOverrides: {
              root: {
                backgroundColor: '#ffffff',
                color: '#1e293b',
                boxShadow: '0 1px 3px 0 rgb(0 0 0 / 0.08)',
              },
            },
          },
          MuiChip: {
            styleOverrides: {
              root: { borderRadius: 8, fontWeight: 500 },
              colorPrimary: { backgroundColor: '#f0f9ff', color: '#0284c7' },
              colorSecondary: { backgroundColor: '#eef2ff', color: '#4f46e5' },
              colorSuccess: { backgroundColor: '#f0fdf4', color: '#16a34a' },
              colorWarning: { backgroundColor: '#fffbeb', color: '#d97706' },
              colorError: { backgroundColor: '#fef2f2', color: '#dc2626' },
            },
          },
          MuiStepper: {
            styleOverrides: { root: { padding: '24px 0' } },
          },
          MuiStepLabel: {
            styleOverrides: {
              label: {
                fontWeight: 500,
                '&.Mui-active': { fontWeight: 600 },
                '&.Mui-completed': { fontWeight: 600 },
              },
            },
          },
          MuiStepIcon: {
            styleOverrides: {
              root: {
                fontSize: '2rem',
                '&.Mui-active': { color: '#0ea5e9' },
                '&.Mui-completed': { color: '#22c55e' },
              },
            },
          },
          MuiAlert: {
            styleOverrides: {
              root: { borderRadius: 12, padding: '16px 20px' },
              standardSuccess: {
                backgroundColor: '#f0fdf4',
                color: '#166534',
                '& .MuiAlert-icon': { color: '#22c55e' },
              },
              standardInfo: {
                backgroundColor: '#f0f9ff',
                color: '#075985',
                '& .MuiAlert-icon': { color: '#0ea5e9' },
              },
              standardWarning: {
                backgroundColor: '#fffbeb',
                color: '#92400e',
                '& .MuiAlert-icon': { color: '#f59e0b' },
              },
              standardError: {
                backgroundColor: '#fef2f2',
                color: '#991b1b',
                '& .MuiAlert-icon': { color: '#ef4444' },
              },
            },
          },
          MuiLinearProgress: {
            styleOverrides: {
              root: { borderRadius: 4, height: 6, backgroundColor: '#e2e8f0' },
              bar: { borderRadius: 4 },
            },
          },
          MuiTooltip: {
            styleOverrides: {
              tooltip: {
                backgroundColor: '#1e293b',
                borderRadius: 8,
                padding: '8px 12px',
                fontSize: '0.8125rem',
              },
            },
          },
          MuiDialog: {
            styleOverrides: { paper: { borderRadius: 20, padding: '8px' } },
          },
          MuiDialogTitle: {
            styleOverrides: { root: { fontSize: '1.25rem', fontWeight: 600, padding: '20px 24px' } },
          },
          MuiDialogContent: {
            styleOverrides: { root: { padding: '20px 24px' } },
          },
          MuiDialogActions: {
            styleOverrides: { root: { padding: '16px 24px' } },
          },
          MuiDivider: {
            styleOverrides: { root: { borderColor: '#f1f5f9' } },
          },
          MuiListItemButton: {
            styleOverrides: {
              root: {
                borderRadius: 12,
                '&.Mui-selected': {
                  backgroundColor: '#f0f9ff',
                  color: '#0284c7',
                  '&:hover': { backgroundColor: '#e0f2fe' },
                },
              },
            },
          },
          MuiIconButton: {
            styleOverrides: {
              root: {
                borderRadius: 12,
                transition: 'all 0.2s ease',
                '&:hover': { backgroundColor: '#f1f5f9' },
              },
            },
          },
        },
      }),
    [rtl]
  )

  return (
    <ThemeProvider theme={theme}>
      <CssBaseline />
      <LocalizationProvider dateAdapter={AdapterDateFns}>
        <Layout>
          <Routes>
            <Route path="/" element={<HomePage />} />
            <Route path="/report" element={<FnolFormPage />} />
            <Route path="/success/:fnolId" element={<SuccessPage />} />
            <Route path="/track" element={<TrackingPage />} />
            <Route path="/track/:fnolId" element={<TrackingPage />} />
          </Routes>
        </Layout>
      </LocalizationProvider>
    </ThemeProvider>
  )
}

export default App
