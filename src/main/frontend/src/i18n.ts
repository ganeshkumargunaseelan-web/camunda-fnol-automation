/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 *
 * ═══════════════════════════════════════════════════════════════════════════════
 * SUPPORTED LANGUAGES & ENCODING REFERENCE
 * ═══════════════════════════════════════════════════════════════════════════════
 *
 * ┌──────┬─────────────────┬──────────────┬───────────┬─────────────────────────┐
 * │ Code │ Language        │ Native Name  │ Direction │ Script / Encoding       │
 * ├──────┼─────────────────┼──────────────┼───────────┼─────────────────────────┤
 * │ en   │ English         │ English      │ LTR →     │ Latin (UTF-8)           │
 * │ ar   │ Arabic          │ العربية      │ RTL ←     │ Arabic Script (UTF-8)   │
 * │ hi   │ Hindi           │ हिन्दी        │ LTR →     │ Devanagari (UTF-8)      │
 * │ ur   │ Urdu            │ اردو         │ RTL ←     │ Nastaliq/Arabic (UTF-8) │
 * │ ml   │ Malayalam       │ മലയാളം       │ LTR →     │ Malayalam Script (UTF-8)│
 * │ tl   │ Tagalog/Filipino│ Filipino     │ LTR →     │ Latin (UTF-8)           │
 * └──────┴─────────────────┴──────────────┴───────────┴─────────────────────────┘
 *
 * TARGET AUDIENCE (GCC Region):
 * ─────────────────────────────
 * • Arabic (ar)    - Native GCC population (UAE, Saudi, Qatar, Kuwait, Bahrain, Oman)
 * • English (en)   - International community, business language
 * • Hindi (hi)     - Indian expatriate community (largest expat group)
 * • Urdu (ur)      - Pakistani expatriate community
 * • Malayalam (ml) - Kerala (South India) expatriate community
 * • Tagalog (tl)   - Filipino expatriate community
 *
 * RTL (Right-to-Left) LANGUAGES:
 * ──────────────────────────────
 * • Arabic (ar)  - Primary RTL language
 * • Urdu (ur)    - Uses Arabic script with Nastaliq style
 *
 * All translation files are encoded in UTF-8 for proper Unicode support.
 * ═══════════════════════════════════════════════════════════════════════════════
 */

import i18n from 'i18next'
import { initReactI18next } from 'react-i18next'
import LanguageDetector from 'i18next-browser-languagedetector'

// Import translation files (UTF-8 encoded JSON)
import en from './locales/en.json'
import ar from './locales/ar.json'
import hi from './locales/hi.json'
import ur from './locales/ur.json'
import ml from './locales/ml.json'
import tl from './locales/tl.json'

/**
 * Language metadata for UI display and configuration
 */
export interface LanguageConfig {
  code: string
  name: string
  nativeName: string
  direction: 'ltr' | 'rtl'
  script: string
  flag: string
}

/**
 * Complete language configuration with metadata
 * Used for language selector UI and RTL detection
 */
export const SUPPORTED_LANGUAGES: LanguageConfig[] = [
  {
    code: 'en',
    name: 'English',
    nativeName: 'English',
    direction: 'ltr',
    script: 'Latin',
    flag: '🇬🇧',
  },
  {
    code: 'ar',
    name: 'Arabic',
    nativeName: 'العربية',
    direction: 'rtl',
    script: 'Arabic',
    flag: '🇸🇦',
  },
  {
    code: 'hi',
    name: 'Hindi',
    nativeName: 'हिन्दी',
    direction: 'ltr',
    script: 'Devanagari',
    flag: '🇮🇳',
  },
  {
    code: 'ur',
    name: 'Urdu',
    nativeName: 'اردو',
    direction: 'rtl',
    script: 'Nastaliq',
    flag: '🇵🇰',
  },
  {
    code: 'ml',
    name: 'Malayalam',
    nativeName: 'മലയാളം',
    direction: 'ltr',
    script: 'Malayalam',
    flag: '🇮🇳',
  },
  {
    code: 'tl',
    name: 'Tagalog',
    nativeName: 'Filipino',
    direction: 'ltr',
    script: 'Latin',
    flag: '🇵🇭',
  },
]

/**
 * i18next resources configuration
 */
const resources = {
  en: { translation: en },
  ar: { translation: ar },
  hi: { translation: hi },
  ur: { translation: ur },
  ml: { translation: ml },
  tl: { translation: tl },
}

/**
 * List of supported language codes
 */
export const SUPPORTED_LANGUAGE_CODES = SUPPORTED_LANGUAGES.map((lang) => lang.code)

/**
 * List of RTL language codes
 */
export const RTL_LANGUAGES = SUPPORTED_LANGUAGES
  .filter((lang) => lang.direction === 'rtl')
  .map((lang) => lang.code)

/**
 * Initialize i18next with language detection and React binding
 */
i18n
  .use(LanguageDetector)
  .use(initReactI18next)
  .init({
    resources,
    fallbackLng: 'en',
    supportedLngs: SUPPORTED_LANGUAGE_CODES,
    interpolation: {
      escapeValue: false, // React already escapes values
    },
    detection: {
      order: ['querystring', 'localStorage', 'navigator'],
      caches: ['localStorage'],
    },
  })

export default i18n

/**
 * Check if a language uses RTL (Right-to-Left) script direction
 * @param lang - Language code (e.g., 'ar', 'en')
 * @returns true if the language is RTL
 */
export const isRTL = (lang: string): boolean => {
  return RTL_LANGUAGES.includes(lang)
}

/**
 * Get language configuration by code
 * @param code - Language code (e.g., 'ar', 'en')
 * @returns LanguageConfig object or undefined
 */
export const getLanguageConfig = (code: string): LanguageConfig | undefined => {
  return SUPPORTED_LANGUAGES.find((lang) => lang.code === code)
}

/**
 * Get the text direction for a language
 * @param lang - Language code
 * @returns 'rtl' or 'ltr'
 */
export const getDirection = (lang: string): 'ltr' | 'rtl' => {
  return isRTL(lang) ? 'rtl' : 'ltr'
}
