/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
import { useState, useEffect } from 'react'
import { useParams, useNavigate } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import {
  Box,
  Button,
  Card,
  CardContent,
  Chip,
  CircularProgress,
  Grid,
  TextField,
  Typography,
  Alert,
  Avatar,
  Divider,
  InputAdornment,
  LinearProgress,
} from '@mui/material'
import SearchIcon from '@mui/icons-material/Search'
import TrackChangesIcon from '@mui/icons-material/TrackChanges'
import AccessTimeIcon from '@mui/icons-material/AccessTime'
import SpeedIcon from '@mui/icons-material/Speed'
import RouteIcon from '@mui/icons-material/Route'
import CalendarTodayIcon from '@mui/icons-material/CalendarToday'
import UpdateIcon from '@mui/icons-material/Update'
import { getFnolStatus, FnolStatusResponse } from '../services/api'

function TrackingPage() {
  const { t } = useTranslation()
  const { fnolId: urlFnolId } = useParams<{ fnolId: string }>()
  const navigate = useNavigate()

  const [fnolId, setFnolId] = useState(urlFnolId || '')
  const [loading, setLoading] = useState(false)
  const [error, setError] = useState<string | null>(null)
  const [status, setStatus] = useState<FnolStatusResponse | null>(null)

  useEffect(() => {
    if (urlFnolId) {
      handleSearch(urlFnolId)
    }
  }, [urlFnolId])

  const handleSearch = async (id?: string) => {
    const searchId = id || fnolId
    if (!searchId) return

    setLoading(true)
    setError(null)

    try {
      const result = await getFnolStatus(searchId)
      setStatus(result)
      if (!urlFnolId && searchId !== urlFnolId) {
        navigate(`/track/${searchId}`, { replace: true })
      }
    } catch (err: any) {
      setError(err.message || 'FNOL not found')
      setStatus(null)
    } finally {
      setLoading(false)
    }
  }

  const getSeverityConfig = (severity: string | undefined) => {
    switch (severity) {
      case 'HIGH':
        return { color: '#ef4444', bgColor: '#fef2f2', label: 'High Priority' }
      case 'MEDIUM':
        return { color: '#f59e0b', bgColor: '#fffbeb', label: 'Medium Priority' }
      case 'LOW':
        return { color: '#22c55e', bgColor: '#f0fdf4', label: 'Low Priority' }
      default:
        return { color: '#6b7280', bgColor: '#f9fafb', label: 'Unknown' }
    }
  }

  const getRouteConfig = (route: string | undefined) => {
    switch (route) {
      case 'complex':
        return { color: '#ef4444', bgColor: '#fef2f2', label: 'Complex Review', progress: 30 }
      case 'standard':
        return { color: '#0ea5e9', bgColor: '#f0f9ff', label: 'Standard Processing', progress: 50 }
      case 'fast-track':
        return { color: '#22c55e', bgColor: '#f0fdf4', label: 'Fast Track', progress: 70 }
      default:
        return { color: '#6b7280', bgColor: '#f9fafb', label: 'Pending', progress: 10 }
    }
  }

  const getStatusConfig = (statusValue: string | undefined) => {
    switch (statusValue) {
      case 'SUBMITTED':
        return { color: '#0ea5e9', bgColor: '#f0f9ff', label: 'Submitted' }
      case 'IN_PROGRESS':
        return { color: '#f59e0b', bgColor: '#fffbeb', label: 'In Progress' }
      case 'COMPLETED':
        return { color: '#22c55e', bgColor: '#f0fdf4', label: 'Completed' }
      case 'REJECTED':
        return { color: '#ef4444', bgColor: '#fef2f2', label: 'Rejected' }
      default:
        return { color: '#6b7280', bgColor: '#f9fafb', label: statusValue || 'Unknown' }
    }
  }

  const severityConfig = getSeverityConfig(status?.severityLevel)
  const routeConfig = getRouteConfig(status?.route)
  const statusConfig = getStatusConfig(status?.status)

  return (
    <Box sx={{ maxWidth: 700, mx: 'auto' }}>
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
          <Avatar
            sx={{
              width: 56,
              height: 56,
              background: 'linear-gradient(135deg, #6366f1 0%, #8b5cf6 100%)',
            }}
          >
            <TrackChangesIcon sx={{ fontSize: 28 }} />
          </Avatar>
          <Box>
            <Typography
              variant="h4"
              fontWeight={700}
              sx={{
                background: 'linear-gradient(135deg, #1e293b 0%, #475569 100%)',
                WebkitBackgroundClip: 'text',
                WebkitTextFillColor: 'transparent',
              }}
            >
              {t('tracking.title')}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Enter your FNOL reference number to check claim status
            </Typography>
          </Box>
        </Box>
      </Box>

      {/* Search Card */}
      <Card
        sx={{
          mb: 4,
          position: 'relative',
          overflow: 'hidden',
          '&::before': {
            content: '""',
            position: 'absolute',
            top: 0,
            left: 0,
            right: 0,
            height: 4,
            background: 'linear-gradient(90deg, #6366f1 0%, #8b5cf6 100%)',
          },
        }}
      >
        <CardContent sx={{ p: 4 }}>
          <Box sx={{ display: 'flex', gap: 2 }}>
            <TextField
              value={fnolId}
              onChange={(e) => setFnolId(e.target.value.toUpperCase())}
              placeholder={t('tracking.enterFnolId')}
              sx={{ flex: 1 }}
              InputProps={{
                startAdornment: (
                  <InputAdornment position="start">
                    <SearchIcon sx={{ color: 'text.secondary' }} />
                  </InputAdornment>
                ),
                sx: {
                  borderRadius: 3,
                  '& input': {
                    fontFamily: 'monospace',
                    fontWeight: 600,
                    letterSpacing: '0.05em',
                  },
                },
              }}
              onKeyPress={(e) => e.key === 'Enter' && handleSearch()}
            />
            <Button
              variant="contained"
              onClick={() => handleSearch()}
              disabled={loading || !fnolId}
              sx={{
                px: 4,
                borderRadius: 3,
                minWidth: 120,
              }}
            >
              {loading ? (
                <CircularProgress size={24} color="inherit" />
              ) : (
                t('tracking.search')
              )}
            </Button>
          </Box>
          <Typography variant="caption" color="text.secondary" sx={{ mt: 2, display: 'block' }}>
            Example: FNOL-AE-2025-000001
          </Typography>
        </CardContent>
      </Card>

      {/* Error Alert */}
      {error && (
        <Alert
          severity="error"
          sx={{ mb: 3, borderRadius: 3 }}
          onClose={() => setError(null)}
        >
          {error}
        </Alert>
      )}

      {/* Results Card */}
      {status && (
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
              height: 4,
              background: `linear-gradient(90deg, ${statusConfig.color} 0%, ${severityConfig.color} 100%)`,
            },
          }}
        >
          <CardContent sx={{ p: 4 }}>
            {/* FNOL ID Header */}
            <Box sx={{ mb: 4 }}>
              <Typography variant="body2" color="text.secondary" fontWeight={500}>
                Claim Reference
              </Typography>
              <Typography
                variant="h5"
                fontWeight={700}
                sx={{ fontFamily: 'monospace', letterSpacing: '0.03em' }}
              >
                {status.fnolId}
              </Typography>
            </Box>

            {/* Progress Bar */}
            <Box sx={{ mb: 4 }}>
              <Box sx={{ display: 'flex', justifyContent: 'space-between', mb: 1 }}>
                <Typography variant="body2" fontWeight={600}>
                  Claim Progress
                </Typography>
                <Typography variant="body2" color="text.secondary">
                  {routeConfig.progress}%
                </Typography>
              </Box>
              <LinearProgress
                variant="determinate"
                value={routeConfig.progress}
                sx={{
                  height: 8,
                  borderRadius: 4,
                  bgcolor: 'grey.200',
                  '& .MuiLinearProgress-bar': {
                    borderRadius: 4,
                    background: `linear-gradient(90deg, ${routeConfig.color} 0%, ${statusConfig.color} 100%)`,
                  },
                }}
              />
            </Box>

            <Divider sx={{ my: 3 }} />

            {/* Status Grid */}
            <Grid container spacing={3}>
              {/* Status */}
              <Grid item xs={6}>
                <Box sx={{ display: 'flex', alignItems: 'flex-start', gap: 2 }}>
                  <Avatar
                    sx={{
                      width: 40,
                      height: 40,
                      bgcolor: statusConfig.bgColor,
                    }}
                  >
                    <AccessTimeIcon sx={{ color: statusConfig.color, fontSize: 20 }} />
                  </Avatar>
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      {t('tracking.status')}
                    </Typography>
                    <Chip
                      label={statusConfig.label}
                      size="small"
                      sx={{
                        mt: 0.5,
                        bgcolor: statusConfig.bgColor,
                        color: statusConfig.color,
                        fontWeight: 600,
                      }}
                    />
                  </Box>
                </Box>
              </Grid>

              {/* Severity */}
              <Grid item xs={6}>
                <Box sx={{ display: 'flex', alignItems: 'flex-start', gap: 2 }}>
                  <Avatar
                    sx={{
                      width: 40,
                      height: 40,
                      bgcolor: severityConfig.bgColor,
                    }}
                  >
                    <SpeedIcon sx={{ color: severityConfig.color, fontSize: 20 }} />
                  </Avatar>
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      {t('tracking.severity')}
                    </Typography>
                    <Chip
                      label={severityConfig.label}
                      size="small"
                      sx={{
                        mt: 0.5,
                        bgcolor: severityConfig.bgColor,
                        color: severityConfig.color,
                        fontWeight: 600,
                      }}
                    />
                  </Box>
                </Box>
              </Grid>

              {/* Route */}
              <Grid item xs={6}>
                <Box sx={{ display: 'flex', alignItems: 'flex-start', gap: 2 }}>
                  <Avatar
                    sx={{
                      width: 40,
                      height: 40,
                      bgcolor: routeConfig.bgColor,
                    }}
                  >
                    <RouteIcon sx={{ color: routeConfig.color, fontSize: 20 }} />
                  </Avatar>
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      {t('tracking.route')}
                    </Typography>
                    <Chip
                      label={routeConfig.label}
                      size="small"
                      variant="outlined"
                      sx={{
                        mt: 0.5,
                        borderColor: routeConfig.color,
                        color: routeConfig.color,
                        fontWeight: 600,
                      }}
                    />
                  </Box>
                </Box>
              </Grid>

              {/* Submitted Date */}
              <Grid item xs={6}>
                <Box sx={{ display: 'flex', alignItems: 'flex-start', gap: 2 }}>
                  <Avatar
                    sx={{
                      width: 40,
                      height: 40,
                      bgcolor: '#f3f4f6',
                    }}
                  >
                    <CalendarTodayIcon sx={{ color: '#6b7280', fontSize: 20 }} />
                  </Avatar>
                  <Box>
                    <Typography variant="body2" color="text.secondary">
                      {t('tracking.submitted')}
                    </Typography>
                    <Typography variant="body1" fontWeight={600} sx={{ mt: 0.5 }}>
                      {new Date(status.createdAt).toLocaleDateString()}
                    </Typography>
                  </Box>
                </Box>
              </Grid>

              {/* Last Updated */}
              {status.updatedAt && (
                <Grid item xs={12}>
                  <Box
                    sx={{
                      display: 'flex',
                      alignItems: 'center',
                      gap: 2,
                      bgcolor: '#f8fafc',
                      p: 2,
                      borderRadius: 2,
                    }}
                  >
                    <UpdateIcon sx={{ color: 'text.secondary' }} />
                    <Box>
                      <Typography variant="body2" color="text.secondary">
                        {t('tracking.lastUpdated')}
                      </Typography>
                      <Typography variant="body2" fontWeight={600}>
                        {new Date(status.updatedAt).toLocaleString()}
                      </Typography>
                    </Box>
                  </Box>
                </Grid>
              )}
            </Grid>
          </CardContent>
        </Card>
      )}

      {/* Empty State */}
      {!status && !loading && !error && (
        <Box
          sx={{
            textAlign: 'center',
            py: 8,
            px: 4,
            bgcolor: '#f8fafc',
            borderRadius: 4,
            border: '2px dashed',
            borderColor: 'grey.300',
          }}
        >
          <Avatar
            sx={{
              width: 80,
              height: 80,
              bgcolor: '#f1f5f9',
              mx: 'auto',
              mb: 3,
            }}
          >
            <SearchIcon sx={{ fontSize: 40, color: '#94a3b8' }} />
          </Avatar>
          <Typography variant="h6" fontWeight={600} color="text.secondary" gutterBottom>
            Search for your claim
          </Typography>
          <Typography variant="body2" color="text.secondary">
            Enter your FNOL reference number above to view the status of your claim
          </Typography>
        </Box>
      )}
    </Box>
  )
}

export default TrackingPage
