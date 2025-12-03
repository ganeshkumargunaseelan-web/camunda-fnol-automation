/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
import { useNavigate } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import {
  Box,
  Button,
  Card,
  CardContent,
  Grid,
  Typography,
  Chip,
  Avatar,
} from '@mui/material'
import AddCircleOutlineIcon from '@mui/icons-material/AddCircleOutline'
import SearchIcon from '@mui/icons-material/Search'
import ArrowForwardIcon from '@mui/icons-material/ArrowForward'
import SecurityIcon from '@mui/icons-material/Security'
import SpeedIcon from '@mui/icons-material/Speed'
import SupportAgentIcon from '@mui/icons-material/SupportAgent'
import VerifiedIcon from '@mui/icons-material/Verified'

function HomePage() {
  const { t } = useTranslation()
  const navigate = useNavigate()

  const features = [
    {
      icon: <SpeedIcon />,
      title: 'Fast Processing',
      description: 'Submit claims in minutes with our streamlined digital process.',
      color: '#0ea5e9',
      bgColor: '#f0f9ff',
    },
    {
      icon: <SecurityIcon />,
      title: 'Secure & Private',
      description: 'Your data is protected with enterprise-grade security.',
      color: '#22c55e',
      bgColor: '#f0fdf4',
    },
    {
      icon: <SupportAgentIcon />,
      title: 'Multi-language',
      description: 'Available in Arabic, English, Hindi, Urdu, and more.',
      color: '#6366f1',
      bgColor: '#eef2ff',
    },
    {
      icon: <VerifiedIcon />,
      title: 'GCC Compliant',
      description: 'Built for UAE, Saudi Arabia, Qatar, Kuwait, Bahrain & Oman.',
      color: '#f59e0b',
      bgColor: '#fffbeb',
    },
  ]

  const gccCountries = [
    { code: 'AE', name: 'UAE', flag: '🇦🇪' },
    { code: 'SA', name: 'Saudi Arabia', flag: '🇸🇦' },
    { code: 'QA', name: 'Qatar', flag: '🇶🇦' },
    { code: 'KW', name: 'Kuwait', flag: '🇰🇼' },
    { code: 'BH', name: 'Bahrain', flag: '🇧🇭' },
    { code: 'OM', name: 'Oman', flag: '🇴🇲' },
  ]

  return (
    <Box>
      {/* Hero Section */}
      <Box
        sx={{
          textAlign: 'center',
          py: { xs: 4, md: 8 },
          mb: { xs: 4, md: 6 },
        }}
      >
        <Chip
          label="GCC Motor Insurance Claims"
          color="primary"
          sx={{
            mb: 3,
            px: 2,
            py: 2.5,
            fontSize: '0.85rem',
            fontWeight: 600,
            borderRadius: 3,
            bgcolor: 'primary.light',
          }}
        />
        <Typography
          variant="h2"
          component="h1"
          sx={{
            fontWeight: 800,
            mb: 2,
            fontSize: { xs: '2rem', md: '3rem' },
            background: 'linear-gradient(135deg, #1e293b 0%, #475569 100%)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent',
            lineHeight: 1.2,
          }}
        >
          Report Your Motor Claim
          <br />
          <Box component="span" sx={{
            background: 'linear-gradient(135deg, #0ea5e9 0%, #6366f1 100%)',
            WebkitBackgroundClip: 'text',
            WebkitTextFillColor: 'transparent',
          }}>
            Quick, Easy & Secure
          </Box>
        </Typography>
        <Typography
          variant="h6"
          color="text.secondary"
          sx={{
            mb: 5,
            maxWidth: 600,
            mx: 'auto',
            fontWeight: 400,
            lineHeight: 1.6,
          }}
        >
          File your First Notice of Loss (FNOL) digitally. Get faster processing
          with real-time updates across all GCC countries.
        </Typography>

        {/* CTA Buttons */}
        <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
          <Button
            variant="contained"
            size="large"
            onClick={() => navigate('/report')}
            endIcon={<ArrowForwardIcon />}
            sx={{
              px: 4,
              py: 1.5,
              fontSize: '1rem',
            }}
          >
            Report New Claim
          </Button>
          <Button
            variant="outlined"
            size="large"
            onClick={() => navigate('/track')}
            startIcon={<SearchIcon />}
            sx={{
              px: 4,
              py: 1.5,
              fontSize: '1rem',
            }}
          >
            Track Existing Claim
          </Button>
        </Box>
      </Box>

      {/* Action Cards */}
      <Grid container spacing={3} sx={{ mb: { xs: 6, md: 8 } }}>
        <Grid item xs={12} md={6}>
          <Card
            sx={{
              height: '100%',
              cursor: 'pointer',
              position: 'relative',
              overflow: 'hidden',
              '&::before': {
                content: '""',
                position: 'absolute',
                top: 0,
                left: 0,
                right: 0,
                height: 4,
                background: 'linear-gradient(90deg, #0ea5e9 0%, #38bdf8 100%)',
              },
            }}
            onClick={() => navigate('/report')}
          >
            <CardContent sx={{ p: 4 }}>
              <Avatar
                sx={{
                  width: 64,
                  height: 64,
                  bgcolor: '#f0f9ff',
                  color: '#0ea5e9',
                  mb: 3,
                }}
              >
                <AddCircleOutlineIcon sx={{ fontSize: 32 }} />
              </Avatar>
              <Typography variant="h5" fontWeight={600} gutterBottom>
                {t('nav.newClaim')}
              </Typography>
              <Typography variant="body1" color="text.secondary" sx={{ mb: 3, lineHeight: 1.7 }}>
                Start a new motor insurance claim. Our guided wizard will walk you through
                the process step by step. Takes only 5-10 minutes.
              </Typography>
              <Button
                variant="contained"
                endIcon={<ArrowForwardIcon />}
                sx={{ mt: 1 }}
              >
                Start Claim
              </Button>
            </CardContent>
          </Card>
        </Grid>

        <Grid item xs={12} md={6}>
          <Card
            sx={{
              height: '100%',
              cursor: 'pointer',
              position: 'relative',
              overflow: 'hidden',
              '&::before': {
                content: '""',
                position: 'absolute',
                top: 0,
                left: 0,
                right: 0,
                height: 4,
                background: 'linear-gradient(90deg, #6366f1 0%, #818cf8 100%)',
              },
            }}
            onClick={() => navigate('/track')}
          >
            <CardContent sx={{ p: 4 }}>
              <Avatar
                sx={{
                  width: 64,
                  height: 64,
                  bgcolor: '#eef2ff',
                  color: '#6366f1',
                  mb: 3,
                }}
              >
                <SearchIcon sx={{ fontSize: 32 }} />
              </Avatar>
              <Typography variant="h5" fontWeight={600} gutterBottom>
                {t('nav.trackClaim')}
              </Typography>
              <Typography variant="body1" color="text.secondary" sx={{ mb: 3, lineHeight: 1.7 }}>
                Check the status of your existing claim using your FNOL reference number.
                Get real-time updates on your claim progress.
              </Typography>
              <Button
                variant="outlined"
                color="secondary"
                endIcon={<ArrowForwardIcon />}
                sx={{ mt: 1 }}
              >
                Track Status
              </Button>
            </CardContent>
          </Card>
        </Grid>
      </Grid>

      {/* Features Section */}
      <Box sx={{ mb: { xs: 6, md: 8 } }}>
        <Typography
          variant="h4"
          textAlign="center"
          fontWeight={700}
          sx={{ mb: 1 }}
        >
          Why Choose Our Platform?
        </Typography>
        <Typography
          variant="body1"
          textAlign="center"
          color="text.secondary"
          sx={{ mb: 5, maxWidth: 500, mx: 'auto' }}
        >
          Built specifically for GCC motor insurance with industry-leading features
        </Typography>

        <Grid container spacing={3}>
          {features.map((feature, index) => (
            <Grid item xs={12} sm={6} md={3} key={index}>
              <Card
                sx={{
                  height: '100%',
                  textAlign: 'center',
                  transition: 'transform 0.2s ease',
                  '&:hover': {
                    transform: 'translateY(-4px)',
                  },
                }}
              >
                <CardContent sx={{ p: 3 }}>
                  <Avatar
                    sx={{
                      width: 56,
                      height: 56,
                      bgcolor: feature.bgColor,
                      color: feature.color,
                      mx: 'auto',
                      mb: 2,
                    }}
                  >
                    {feature.icon}
                  </Avatar>
                  <Typography variant="h6" fontWeight={600} gutterBottom>
                    {feature.title}
                  </Typography>
                  <Typography variant="body2" color="text.secondary">
                    {feature.description}
                  </Typography>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      </Box>

      {/* Supported Countries */}
      <Box
        sx={{
          textAlign: 'center',
          py: 5,
          px: 4,
          bgcolor: 'white',
          borderRadius: 4,
          border: '1px solid',
          borderColor: 'divider',
        }}
      >
        <Typography variant="h6" fontWeight={600} gutterBottom>
          Supported Across All GCC Countries
        </Typography>
        <Typography variant="body2" color="text.secondary" sx={{ mb: 3 }}>
          Country-specific validation for mobile numbers, national IDs, and plate numbers
        </Typography>
        <Box sx={{ display: 'flex', justifyContent: 'center', gap: 2, flexWrap: 'wrap' }}>
          {gccCountries.map((country) => (
            <Chip
              key={country.code}
              label={
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <span style={{ fontSize: '1.1rem' }}>{country.flag}</span>
                  <span>{country.name}</span>
                </Box>
              }
              sx={{
                px: 1.5,
                py: 2.5,
                fontSize: '0.9rem',
                fontWeight: 500,
                borderRadius: 3,
                bgcolor: 'grey.100',
                border: '1px solid',
                borderColor: 'grey.200',
                transition: 'all 0.2s ease',
                '&:hover': {
                  bgcolor: 'primary.light',
                  borderColor: 'primary.main',
                },
              }}
            />
          ))}
        </Box>
      </Box>
    </Box>
  )
}

export default HomePage
