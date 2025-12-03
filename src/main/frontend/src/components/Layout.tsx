/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
import { ReactNode, useState } from 'react'
import { useNavigate, useLocation } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import {
  AppBar,
  Box,
  Container,
  Drawer,
  IconButton,
  List,
  ListItem,
  ListItemButton,
  ListItemIcon,
  ListItemText,
  Menu,
  MenuItem,
  Toolbar,
  Typography,
  useTheme,
  useMediaQuery,
  Avatar,
  Divider,
  Chip,
} from '@mui/material'
import MenuIcon from '@mui/icons-material/Menu'
import HomeIcon from '@mui/icons-material/Home'
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline'
import SearchIcon from '@mui/icons-material/Search'
import LanguageIcon from '@mui/icons-material/Language'
import DirectionsCarIcon from '@mui/icons-material/DirectionsCar'
import KeyboardArrowDownIcon from '@mui/icons-material/KeyboardArrowDown'

interface LayoutProps {
  children: ReactNode
}

const languages = [
  { code: 'en', name: 'English', nativeName: 'English', flag: '🇬🇧' },
  { code: 'ar', name: 'Arabic', nativeName: 'العربية', flag: '🇸🇦' },
  { code: 'hi', name: 'Hindi', nativeName: 'हिन्दी', flag: '🇮🇳' },
  { code: 'ur', name: 'Urdu', nativeName: 'اردو', flag: '🇵🇰' },
  { code: 'ml', name: 'Malayalam', nativeName: 'മലയാളം', flag: '🇮🇳' },
  { code: 'tl', name: 'Tagalog', nativeName: 'Tagalog', flag: '🇵🇭' },
]

function Layout({ children }: LayoutProps) {
  const { t, i18n } = useTranslation()
  const navigate = useNavigate()
  const location = useLocation()
  const theme = useTheme()
  const isMobile = useMediaQuery(theme.breakpoints.down('md'))

  const [drawerOpen, setDrawerOpen] = useState(false)
  const [langAnchor, setLangAnchor] = useState<null | HTMLElement>(null)

  const navItems = [
    { path: '/', label: t('nav.home'), icon: <HomeIcon /> },
    { path: '/report', label: t('nav.newClaim'), icon: <AddCircleOutlineIcon /> },
    { path: '/track', label: t('nav.trackClaim'), icon: <SearchIcon /> },
  ]

  const handleNavClick = (path: string) => {
    navigate(path)
    setDrawerOpen(false)
  }

  const handleLanguageChange = (langCode: string) => {
    i18n.changeLanguage(langCode)
    setLangAnchor(null)
  }

  const currentLanguage = languages.find((l) => l.code === i18n.language) || languages[0]

  return (
    <Box sx={{ display: 'flex', flexDirection: 'column', minHeight: '100vh', bgcolor: 'background.default' }}>
      {/* Premium App Bar */}
      <AppBar position="sticky" elevation={0}>
        <Container maxWidth="lg">
          <Toolbar sx={{ px: { xs: 0 }, minHeight: { xs: 64, md: 72 } }}>
            {/* Mobile Menu Button */}
            {isMobile && (
              <IconButton
                edge="start"
                onClick={() => setDrawerOpen(true)}
                sx={{ mr: 1 }}
              >
                <MenuIcon />
              </IconButton>
            )}

            {/* Logo */}
            <Box
              sx={{
                display: 'flex',
                alignItems: 'center',
                gap: 1.5,
                cursor: 'pointer',
                mr: 4,
              }}
              onClick={() => navigate('/')}
            >
              <Avatar
                sx={{
                  width: 40,
                  height: 40,
                  background: 'linear-gradient(135deg, #0ea5e9 0%, #6366f1 100%)',
                  fontSize: '1.1rem',
                  fontWeight: 700,
                }}
              >
                <DirectionsCarIcon sx={{ fontSize: 22 }} />
              </Avatar>
              <Box sx={{ display: { xs: 'none', sm: 'block' } }}>
                <Typography
                  variant="h6"
                  sx={{
                    fontWeight: 700,
                    color: 'text.primary',
                    lineHeight: 1.2,
                    letterSpacing: '-0.02em',
                  }}
                >
                  <Box component="span" sx={{ color: 'primary.main' }}>GCC</Box> Motor FNOL
                </Typography>
                <Typography
                  variant="caption"
                  sx={{ color: 'text.secondary', fontSize: '0.7rem', letterSpacing: '0.05em' }}
                >
                  INSURANCE CLAIMS PORTAL
                </Typography>
              </Box>
            </Box>

            {/* Desktop Navigation */}
            {!isMobile && (
              <Box sx={{ display: 'flex', gap: 1, flex: 1 }}>
                {navItems.map((item) => (
                  <Box
                    key={item.path}
                    onClick={() => handleNavClick(item.path)}
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: 1,
                      px: 2,
                      py: 1,
                      borderRadius: 2,
                      cursor: 'pointer',
                      transition: 'all 0.2s ease',
                      bgcolor: location.pathname === item.path ? 'primary.main' : 'transparent',
                      color: location.pathname === item.path ? 'white' : 'text.secondary',
                      '&:hover': {
                        bgcolor: location.pathname === item.path ? 'primary.dark' : 'action.hover',
                        color: location.pathname === item.path ? 'white' : 'text.primary',
                      },
                    }}
                  >
                    {item.icon}
                    <Typography variant="body2" fontWeight={600}>
                      {item.label}
                    </Typography>
                  </Box>
                ))}
              </Box>
            )}

            <Box sx={{ flexGrow: 1 }} />

            {/* Language Selector */}
            <Chip
              icon={<LanguageIcon sx={{ fontSize: 18 }} />}
              label={
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 0.5 }}>
                  <span>{currentLanguage.flag}</span>
                  <span>{currentLanguage.nativeName}</span>
                  <KeyboardArrowDownIcon sx={{ fontSize: 16, ml: 0.5 }} />
                </Box>
              }
              onClick={(e) => setLangAnchor(e.currentTarget)}
              sx={{
                height: 40,
                px: 1,
                borderRadius: 3,
                bgcolor: 'grey.100',
                border: '1px solid',
                borderColor: 'grey.200',
                cursor: 'pointer',
                transition: 'all 0.2s ease',
                '&:hover': {
                  bgcolor: 'grey.200',
                  borderColor: 'grey.300',
                },
                '& .MuiChip-label': {
                  px: 1,
                  fontWeight: 500,
                },
              }}
            />
          </Toolbar>
        </Container>
      </AppBar>

      {/* Mobile Drawer */}
      <Drawer
        anchor={theme.direction === 'rtl' ? 'right' : 'left'}
        open={drawerOpen}
        onClose={() => setDrawerOpen(false)}
        PaperProps={{
          sx: {
            width: 280,
            borderRadius: 0,
            bgcolor: 'background.paper',
          },
        }}
      >
        <Box sx={{ p: 3 }}>
          <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5, mb: 2 }}>
            <Avatar
              sx={{
                width: 44,
                height: 44,
                background: 'linear-gradient(135deg, #0ea5e9 0%, #6366f1 100%)',
              }}
            >
              <DirectionsCarIcon />
            </Avatar>
            <Box>
              <Typography variant="subtitle1" fontWeight={700}>
                <Box component="span" sx={{ color: 'primary.main' }}>GCC</Box> Motor FNOL
              </Typography>
              <Typography variant="caption" color="text.secondary">
                Insurance Claims
              </Typography>
            </Box>
          </Box>
        </Box>
        <Divider />
        <Box sx={{ p: 2 }}>
          <List>
            {navItems.map((item) => (
              <ListItem key={item.path} disablePadding sx={{ mb: 0.5 }}>
                <ListItemButton
                  selected={location.pathname === item.path}
                  onClick={() => handleNavClick(item.path)}
                  sx={{
                    borderRadius: 2,
                    '&.Mui-selected': {
                      bgcolor: 'primary.main',
                      color: 'white',
                      '&:hover': {
                        bgcolor: 'primary.dark',
                      },
                      '& .MuiListItemIcon-root': {
                        color: 'white',
                      },
                    },
                  }}
                >
                  <ListItemIcon sx={{ minWidth: 40 }}>{item.icon}</ListItemIcon>
                  <ListItemText
                    primary={item.label}
                    primaryTypographyProps={{ fontWeight: 600 }}
                  />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Box>
        <Divider />
        <Box sx={{ p: 2 }}>
          <Typography variant="caption" color="text.secondary" sx={{ px: 2, mb: 1, display: 'block' }}>
            Language
          </Typography>
          <List dense>
            {languages.map((lang) => (
              <ListItem key={lang.code} disablePadding>
                <ListItemButton
                  selected={i18n.language === lang.code}
                  onClick={() => {
                    handleLanguageChange(lang.code)
                    setDrawerOpen(false)
                  }}
                  sx={{ borderRadius: 2 }}
                >
                  <ListItemIcon sx={{ minWidth: 36, fontSize: '1.2rem' }}>
                    {lang.flag}
                  </ListItemIcon>
                  <ListItemText primary={lang.nativeName} />
                </ListItemButton>
              </ListItem>
            ))}
          </List>
        </Box>
      </Drawer>

      {/* Language Menu */}
      <Menu
        anchorEl={langAnchor}
        open={Boolean(langAnchor)}
        onClose={() => setLangAnchor(null)}
        PaperProps={{
          sx: {
            mt: 1.5,
            minWidth: 180,
            borderRadius: 3,
            boxShadow: '0 10px 40px -10px rgba(0,0,0,0.2)',
          },
        }}
        transformOrigin={{ horizontal: 'right', vertical: 'top' }}
        anchorOrigin={{ horizontal: 'right', vertical: 'bottom' }}
      >
        {languages.map((lang) => (
          <MenuItem
            key={lang.code}
            selected={i18n.language === lang.code}
            onClick={() => handleLanguageChange(lang.code)}
            sx={{
              py: 1.5,
              px: 2,
              borderRadius: 2,
              mx: 1,
              my: 0.5,
              '&.Mui-selected': {
                bgcolor: 'primary.light',
                '&:hover': {
                  bgcolor: 'primary.light',
                },
              },
            }}
          >
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1.5 }}>
              <span style={{ fontSize: '1.2rem' }}>{lang.flag}</span>
              <Box>
                <Typography variant="body2" fontWeight={600}>
                  {lang.nativeName}
                </Typography>
                <Typography variant="caption" color="text.secondary">
                  {lang.name}
                </Typography>
              </Box>
            </Box>
          </MenuItem>
        ))}
      </Menu>

      {/* Main Content */}
      <Box component="main" sx={{ flex: 1 }}>
        <Container maxWidth="lg" sx={{ py: { xs: 3, md: 5 } }}>
          {children}
        </Container>
      </Box>

      {/* Footer */}
      <Box
        component="footer"
        sx={{
          py: 3,
          px: 2,
          mt: 'auto',
          bgcolor: 'white',
          borderTop: '1px solid',
          borderColor: 'divider',
        }}
      >
        <Container maxWidth="lg">
          <Box
            sx={{
              display: 'flex',
              flexDirection: { xs: 'column', sm: 'row' },
              alignItems: 'center',
              justifyContent: 'space-between',
              gap: 2,
            }}
          >
            <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
              <Avatar
                sx={{
                  width: 28,
                  height: 28,
                  background: 'linear-gradient(135deg, #0ea5e9 0%, #6366f1 100%)',
                  fontSize: '0.75rem',
                }}
              >
                <DirectionsCarIcon sx={{ fontSize: 16 }} />
              </Avatar>
              <Typography variant="body2" color="text.secondary">
                © 2025 GCC Motor FNOL Starter Kit
              </Typography>
            </Box>
            <Box sx={{ display: 'flex', gap: 3 }}>
              <Typography variant="body2" color="text.secondary" sx={{ cursor: 'pointer', '&:hover': { color: 'primary.main' } }}>
                Privacy Policy
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ cursor: 'pointer', '&:hover': { color: 'primary.main' } }}>
                Terms of Service
              </Typography>
              <Typography variant="body2" color="text.secondary" sx={{ cursor: 'pointer', '&:hover': { color: 'primary.main' } }}>
                Support
              </Typography>
            </Box>
          </Box>
        </Container>
      </Box>
    </Box>
  )
}

export default Layout
