/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
import { useState } from 'react'
import { useNavigate } from 'react-router-dom'
import { useTranslation } from 'react-i18next'
import { useForm, FormProvider } from 'react-hook-form'
import {
  Box,
  Button,
  Card,
  CardContent,
  Step,
  StepLabel,
  Stepper,
  Typography,
  Alert,
  CircularProgress,
  Avatar,
  Chip,
} from '@mui/material'
import { v4 as uuidv4 } from 'uuid'
import ArrowBackIcon from '@mui/icons-material/ArrowBack'
import ArrowForwardIcon from '@mui/icons-material/ArrowForward'
import SendIcon from '@mui/icons-material/Send'
import PersonIcon from '@mui/icons-material/Person'
import DirectionsCarIcon from '@mui/icons-material/DirectionsCar'
import ReportProblemIcon from '@mui/icons-material/ReportProblem'
import AttachFileIcon from '@mui/icons-material/AttachFile'
import FactCheckIcon from '@mui/icons-material/FactCheck'

import ContactStep from '../components/form/ContactStep'
import VehicleStep from '../components/form/VehicleStep'
import IncidentStep from '../components/form/IncidentStep'
import AttachmentsStep from '../components/form/AttachmentsStep'
import ReviewStep from '../components/form/ReviewStep'
import { submitFnol, FnolSubmitRequest } from '../services/api'

export interface FnolFormData {
  // Contact
  countryCode: string
  mobileNumber: string
  nationalId: string
  reporterName: string
  reporterEmail: string
  // Vehicle
  plateNumber: string
  plateCountry: string
  vehicleType: string
  vehicleMake: string
  vehicleModel: string
  vehicleYear: number | null
  vehicleColor: string
  policyNumber: string
  coverageType: string
  isFleet: boolean
  // Incident
  incidentDate: string
  incidentTime: string
  incidentLocation: string
  latitude: number | null
  longitude: number | null
  description: string
  isDrivable: boolean
  hasInjuries: boolean
  thirdPartyInvolved: boolean
  policeReportNumber: string
  // Attachments
  attachments: Array<{ url: string; type: string; description: string }>
}

const defaultValues: FnolFormData = {
  countryCode: 'UAE',
  mobileNumber: '',
  nationalId: '',
  reporterName: '',
  reporterEmail: '',
  plateNumber: '',
  plateCountry: 'UAE',
  vehicleType: 'SEDAN',
  vehicleMake: '',
  vehicleModel: '',
  vehicleYear: null,
  vehicleColor: '',
  policyNumber: '',
  coverageType: 'COMPREHENSIVE',
  isFleet: false,
  incidentDate: '',
  incidentTime: '',
  incidentLocation: '',
  latitude: null,
  longitude: null,
  description: '',
  isDrivable: true,
  hasInjuries: false,
  thirdPartyInvolved: false,
  policeReportNumber: '',
  attachments: [],
}

const stepIcons = [
  <PersonIcon />,
  <DirectionsCarIcon />,
  <ReportProblemIcon />,
  <AttachFileIcon />,
  <FactCheckIcon />,
]

const stepColors = ['#0ea5e9', '#6366f1', '#f59e0b', '#22c55e', '#8b5cf6']

function FnolFormPage() {
  const { t, i18n } = useTranslation()
  const navigate = useNavigate()
  const [activeStep, setActiveStep] = useState(0)
  const [submitting, setSubmitting] = useState(false)
  const [error, setError] = useState<string | null>(null)

  const methods = useForm<FnolFormData>({
    defaultValues,
    mode: 'onChange',
  })

  const steps = [
    { key: 'contact', label: t('form.steps.contact') },
    { key: 'vehicle', label: t('form.steps.vehicle') },
    { key: 'incident', label: t('form.steps.incident') },
    { key: 'attachments', label: t('form.steps.attachments') },
    { key: 'review', label: t('form.steps.review') },
  ]

  const handleNext = async () => {
    const isValid = await methods.trigger()
    if (isValid) {
      setActiveStep((prev) => prev + 1)
    }
  }

  const handleBack = () => {
    setActiveStep((prev) => prev - 1)
  }

  const handleSubmit = async (data: FnolFormData) => {
    setSubmitting(true)
    setError(null)

    try {
      const request: FnolSubmitRequest = {
        ...data,
        preferredLanguage: i18n.language.toUpperCase(),
      }

      const idempotencyKey = uuidv4()
      const response = await submitFnol(request, idempotencyKey)

      navigate(`/success/${response.fnolId}`)
    } catch (err: any) {
      setError(err.message || 'An error occurred while submitting your claim')
    } finally {
      setSubmitting(false)
    }
  }

  const renderStepContent = () => {
    switch (activeStep) {
      case 0:
        return <ContactStep />
      case 1:
        return <VehicleStep />
      case 2:
        return <IncidentStep />
      case 3:
        return <AttachmentsStep />
      case 4:
        return <ReviewStep />
      default:
        return null
    }
  }

  return (
    <Box sx={{ maxWidth: 900, mx: 'auto' }}>
      {/* Header */}
      <Box sx={{ mb: 4 }}>
        <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 2 }}>
          <Avatar
            sx={{
              width: 56,
              height: 56,
              background: 'linear-gradient(135deg, #0ea5e9 0%, #6366f1 100%)',
            }}
          >
            <DirectionsCarIcon sx={{ fontSize: 28 }} />
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
              {t('nav.newClaim')}
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Complete all steps to submit your motor insurance claim
            </Typography>
          </Box>
        </Box>
        <Chip
          label={`Step ${activeStep + 1} of ${steps.length}`}
          size="small"
          sx={{
            bgcolor: stepColors[activeStep] + '15',
            color: stepColors[activeStep],
            fontWeight: 600,
          }}
        />
      </Box>

      {/* Premium Stepper */}
      <Card
        sx={{
          mb: 4,
          p: 3,
          background: 'linear-gradient(135deg, #f8fafc 0%, #f1f5f9 100%)',
          border: '1px solid',
          borderColor: 'grey.200',
        }}
      >
        <Stepper activeStep={activeStep} alternativeLabel>
          {steps.map((step, index) => (
            <Step key={step.key}>
              <StepLabel
                StepIconComponent={() => (
                  <Avatar
                    sx={{
                      width: 44,
                      height: 44,
                      bgcolor: index <= activeStep ? stepColors[index] : 'grey.300',
                      color: 'white',
                      transition: 'all 0.3s ease',
                      boxShadow: index === activeStep
                        ? `0 4px 14px ${stepColors[index]}40`
                        : 'none',
                    }}
                  >
                    {stepIcons[index]}
                  </Avatar>
                )}
              >
                <Typography
                  variant="body2"
                  fontWeight={index === activeStep ? 700 : 500}
                  color={index <= activeStep ? 'text.primary' : 'text.secondary'}
                  sx={{ mt: 1 }}
                >
                  {step.label}
                </Typography>
              </StepLabel>
            </Step>
          ))}
        </Stepper>
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

      {/* Form Content */}
      <FormProvider {...methods}>
        <form onSubmit={methods.handleSubmit(handleSubmit)}>
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
                background: `linear-gradient(90deg, ${stepColors[activeStep]} 0%, ${stepColors[(activeStep + 1) % 5]} 100%)`,
              },
            }}
          >
            <CardContent sx={{ p: { xs: 3, md: 5 } }}>
              {/* Step Title */}
              <Box sx={{ mb: 4 }}>
                <Box sx={{ display: 'flex', alignItems: 'center', gap: 2, mb: 1 }}>
                  <Avatar
                    sx={{
                      width: 40,
                      height: 40,
                      bgcolor: stepColors[activeStep] + '15',
                      color: stepColors[activeStep],
                    }}
                  >
                    {stepIcons[activeStep]}
                  </Avatar>
                  <Typography variant="h5" fontWeight={600}>
                    {steps[activeStep].label}
                  </Typography>
                </Box>
                <Typography variant="body2" color="text.secondary" sx={{ ml: 7 }}>
                  {activeStep === 0 && 'Enter your contact information for claim updates'}
                  {activeStep === 1 && 'Provide vehicle and insurance details'}
                  {activeStep === 2 && 'Describe the incident and circumstances'}
                  {activeStep === 3 && 'Upload supporting documents and photos'}
                  {activeStep === 4 && 'Review all information before submission'}
                </Typography>
              </Box>

              {/* Step Content */}
              {renderStepContent()}

              {/* Navigation Buttons */}
              <Box
                sx={{
                  display: 'flex',
                  justifyContent: 'space-between',
                  mt: 5,
                  pt: 3,
                  borderTop: '1px solid',
                  borderColor: 'divider',
                }}
              >
                <Button
                  disabled={activeStep === 0}
                  onClick={handleBack}
                  variant="outlined"
                  startIcon={<ArrowBackIcon />}
                  sx={{
                    px: 3,
                    py: 1.2,
                    borderRadius: 2,
                    opacity: activeStep === 0 ? 0 : 1,
                  }}
                >
                  {t('form.buttons.back')}
                </Button>

                {activeStep === steps.length - 1 ? (
                  <Button
                    type="submit"
                    variant="contained"
                    disabled={submitting}
                    endIcon={submitting ? <CircularProgress size={20} color="inherit" /> : <SendIcon />}
                    sx={{
                      px: 4,
                      py: 1.2,
                      borderRadius: 2,
                      background: 'linear-gradient(135deg, #22c55e 0%, #16a34a 100%)',
                      '&:hover': {
                        background: 'linear-gradient(135deg, #16a34a 0%, #15803d 100%)',
                      },
                    }}
                  >
                    {submitting ? t('app.loading') : t('form.buttons.submit')}
                  </Button>
                ) : (
                  <Button
                    variant="contained"
                    onClick={handleNext}
                    endIcon={<ArrowForwardIcon />}
                    sx={{
                      px: 4,
                      py: 1.2,
                      borderRadius: 2,
                    }}
                  >
                    {t('form.buttons.next')}
                  </Button>
                )}
              </Box>
            </CardContent>
          </Card>
        </form>
      </FormProvider>

      {/* Progress Indicator */}
      <Box sx={{ mt: 3, textAlign: 'center' }}>
        <Typography variant="caption" color="text.secondary">
          Your progress is automatically saved
        </Typography>
      </Box>
    </Box>
  )
}

export default FnolFormPage
