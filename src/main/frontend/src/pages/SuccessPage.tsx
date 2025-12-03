/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
import { useParams, useNavigate } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import {
  Box,
  Button,
  Card,
  CardContent,
  Typography,
  Avatar,
  Divider,
  Grid,
} from '@mui/material'
import CheckCircleIcon from '@mui/icons-material/CheckCircle'
import TrackChangesIcon from '@mui/icons-material/TrackChanges'
import HomeIcon from '@mui/icons-material/Home'
import ContentCopyIcon from '@mui/icons-material/ContentCopy'
import EmailIcon from '@mui/icons-material/Email'
import SmsIcon from '@mui/icons-material/Sms'

function SuccessPage() {
  const { t } = useTranslation()
  const { fnolId } = useParams<{ fnolId: string }>()
  const navigate = useNavigate()

  const handleCopyId = () => {
    if (fnolId) {
      navigator.clipboard.writeText(fnolId)
    }
  }

  return (
    <Box sx={{ maxWidth: 700, mx: 'auto', textAlign: 'center' }}>
      {/* Success Animation Card */}
      <Card
        sx={{
          position: 'relative',
          overflow: 'hidden',
          '&::before': {
            content: '""',
            position: 'absolute',
            top: 0,
            left: 0,
            right: 0,
            height: 6,
            background: 'linear-gradient(90deg, #22c55e 0%, #10b981 100%)',
          },
        }}
      >
        <CardContent sx={{ py: 6, px: { xs: 3, md: 6 } }}>
          {/* Success Icon with Animation */}
          <Box
            sx={{
              display: 'flex',
              justifyContent: 'center',
              mb: 3,
            }}
          >
            <Avatar
              sx={{
                width: 100,
                height: 100,
                bgcolor: '#dcfce7',
                animation: 'pulse 2s infinite',
                '@keyframes pulse': {
                  '0%': { boxShadow: '0 0 0 0 rgba(34, 197, 94, 0.4)' },
                  '70%': { boxShadow: '0 0 0 20px rgba(34, 197, 94, 0)' },
                  '100%': { boxShadow: '0 0 0 0 rgba(34, 197, 94, 0)' },
                },
              }}
            >
              <CheckCircleIcon sx={{ fontSize: 60, color: '#22c55e' }} />
            </Avatar>
          </Box>

          {/* Success Message */}
          <Typography
            variant="h4"
            fontWeight={700}
            sx={{
              mb: 1,
              background: 'linear-gradient(135deg, #166534 0%, #22c55e 100%)',
              WebkitBackgroundClip: 'text',
              WebkitTextFillColor: 'transparent',
            }}
          >
            {t('success.title')}
          </Typography>

          <Typography
            variant="body1"
            color="text.secondary"
            sx={{ mb: 4, maxWidth: 450, mx: 'auto', lineHeight: 1.7 }}
          >
            {t('success.message')}
          </Typography>

          {/* FNOL ID Display */}
          <Box
            sx={{
              background: 'linear-gradient(135deg, #f0fdf4 0%, #dcfce7 100%)',
              py: 3,
              px: 4,
              borderRadius: 4,
              mb: 4,
              border: '2px dashed',
              borderColor: '#86efac',
              position: 'relative',
            }}
          >
            <Typography
              variant="body2"
              color="text.secondary"
              gutterBottom
              fontWeight={500}
            >
              {t('success.fnolId')}
            </Typography>
            <Box sx={{ display: 'flex', alignItems: 'center', justifyContent: 'center', gap: 2 }}>
              <Typography
                variant="h4"
                fontWeight={700}
                sx={{
                  fontFamily: 'monospace',
                  letterSpacing: '0.05em',
                  color: '#166534',
                }}
              >
                {fnolId}
              </Typography>
              <Button
                variant="outlined"
                size="small"
                onClick={handleCopyId}
                startIcon={<ContentCopyIcon />}
                sx={{
                  borderRadius: 2,
                  borderColor: '#86efac',
                  color: '#166534',
                  '&:hover': {
                    borderColor: '#22c55e',
                    bgcolor: '#f0fdf4',
                  },
                }}
              >
                Copy
              </Button>
            </Box>
          </Box>

          {/* Notification Info */}
          <Box
            sx={{
              bgcolor: '#f8fafc',
              borderRadius: 3,
              p: 3,
              mb: 4,
            }}
          >
            <Typography variant="body2" fontWeight={600} sx={{ mb: 2 }}>
              You will receive updates via:
            </Typography>
            <Grid container spacing={2} justifyContent="center">
              <Grid item>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <Avatar sx={{ width: 32, height: 32, bgcolor: '#dbeafe' }}>
                    <EmailIcon sx={{ fontSize: 18, color: '#3b82f6' }} />
                  </Avatar>
                  <Typography variant="body2" color="text.secondary">
                    Email
                  </Typography>
                </Box>
              </Grid>
              <Grid item>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 1 }}>
                  <Avatar sx={{ width: 32, height: 32, bgcolor: '#dcfce7' }}>
                    <SmsIcon sx={{ fontSize: 18, color: '#22c55e' }} />
                  </Avatar>
                  <Typography variant="body2" color="text.secondary">
                    SMS
                  </Typography>
                </Box>
              </Grid>
            </Grid>
          </Box>

          <Divider sx={{ my: 3 }} />

          {/* Action Buttons */}
          <Box sx={{ display: 'flex', gap: 2, justifyContent: 'center', flexWrap: 'wrap' }}>
            <Button
              variant="contained"
              size="large"
              onClick={() => navigate(`/track/${fnolId}`)}
              startIcon={<TrackChangesIcon />}
              sx={{
                px: 4,
                py: 1.5,
                borderRadius: 3,
                background: 'linear-gradient(135deg, #0ea5e9 0%, #0284c7 100%)',
                '&:hover': {
                  background: 'linear-gradient(135deg, #38bdf8 0%, #0ea5e9 100%)',
                },
              }}
            >
              {t('success.trackLink')}
            </Button>
            <Button
              variant="outlined"
              size="large"
              onClick={() => navigate('/')}
              startIcon={<HomeIcon />}
              sx={{
                px: 4,
                py: 1.5,
                borderRadius: 3,
              }}
            >
              {t('nav.home')}
            </Button>
          </Box>
        </CardContent>
      </Card>

      {/* Additional Info */}
      <Box sx={{ mt: 4, textAlign: 'center' }}>
        <Typography variant="body2" color="text.secondary">
          Need help? Contact our support team at{' '}
          <Box
            component="span"
            sx={{ color: 'primary.main', fontWeight: 600, cursor: 'pointer' }}
          >
            support@gccmotorfnol.com
          </Box>
        </Typography>
      </Box>
    </Box>
  )
}

export default SuccessPage
