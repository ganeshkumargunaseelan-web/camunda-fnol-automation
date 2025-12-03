/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
import { useFormContext } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import {
  Box,
  Chip,
  Divider,
  Grid,
  Paper,
  Typography,
} from '@mui/material'
import { FnolFormData } from '../../pages/FnolFormPage'

function ReviewStep() {
  const { t } = useTranslation()
  const { getValues } = useFormContext<FnolFormData>()
  const data = getValues()

  const Section = ({ title, children }: { title: string; children: React.ReactNode }) => (
    <Box sx={{ mb: 3 }}>
      <Typography variant="subtitle1" fontWeight={600} gutterBottom color="primary">
        {title}
      </Typography>
      <Paper variant="outlined" sx={{ p: 2 }}>
        {children}
      </Paper>
    </Box>
  )

  const Field = ({ label, value }: { label: string; value: React.ReactNode }) => (
    <Grid item xs={12} sm={6}>
      <Typography variant="body2" color="text.secondary">
        {label}
      </Typography>
      <Typography variant="body1">{value || '-'}</Typography>
    </Grid>
  )

  return (
    <>
      <Typography variant="h6" gutterBottom>
        {t('form.review.title')}
      </Typography>

      <Section title={t('form.contact.title')}>
        <Grid container spacing={2}>
          <Field label={t('form.contact.country')} value={t(`countries.${data.countryCode}`)} />
          <Field label={t('form.contact.mobileNumber')} value={data.mobileNumber} />
          <Field label={t('form.contact.nationalId')} value={data.nationalId} />
          <Field label={t('form.contact.name')} value={data.reporterName} />
          <Field label={t('form.contact.email')} value={data.reporterEmail} />
        </Grid>
      </Section>

      <Section title={t('form.vehicle.title')}>
        <Grid container spacing={2}>
          <Field label={t('form.vehicle.plateNumber')} value={data.plateNumber} />
          <Field label={t('form.vehicle.vehicleType')} value={t(`vehicleTypes.${data.vehicleType}`)} />
          <Field label={t('form.vehicle.make')} value={data.vehicleMake} />
          <Field label={t('form.vehicle.model')} value={data.vehicleModel} />
          <Field label={t('form.vehicle.year')} value={data.vehicleYear} />
          <Field label={t('form.vehicle.color')} value={data.vehicleColor} />
          <Field label={t('form.vehicle.policyNumber')} value={data.policyNumber} />
          <Field label={t('form.vehicle.coverageType')} value={t(`coverageTypes.${data.coverageType}`)} />
          <Field
            label={t('form.vehicle.isFleet')}
            value={data.isFleet ? 'Yes' : 'No'}
          />
        </Grid>
      </Section>

      <Section title={t('form.incident.title')}>
        <Grid container spacing={2}>
          <Field label={t('form.incident.date')} value={data.incidentDate} />
          <Field label={t('form.incident.time')} value={data.incidentTime} />
          <Field label={t('form.incident.location')} value={data.incidentLocation} />
          <Grid item xs={12}>
            <Typography variant="body2" color="text.secondary">
              {t('form.incident.description')}
            </Typography>
            <Typography variant="body1">{data.description || '-'}</Typography>
          </Grid>
          <Field label={t('form.incident.policeReport')} value={data.policeReportNumber} />
        </Grid>

        <Box sx={{ mt: 2, display: 'flex', gap: 1, flexWrap: 'wrap' }}>
          <Chip
            label={t('form.incident.isDrivable')}
            color={data.isDrivable ? 'success' : 'error'}
            variant="outlined"
          />
          <Chip
            label={t('form.incident.hasInjuries')}
            color={data.hasInjuries ? 'error' : 'success'}
            variant={data.hasInjuries ? 'filled' : 'outlined'}
          />
          <Chip
            label={t('form.incident.thirdParty')}
            color={data.thirdPartyInvolved ? 'warning' : 'default'}
            variant="outlined"
          />
        </Box>
      </Section>

      {data.attachments && data.attachments.length > 0 && (
        <Section title={t('form.attachments.title')}>
          <Typography variant="body2">
            {data.attachments.length} attachment(s) added
          </Typography>
        </Section>
      )}

      <Divider sx={{ my: 3 }} />

      <Typography variant="body2" color="text.secondary">
        Please review your information above before submitting. Once submitted, you will receive
        a reference number to track your claim.
      </Typography>
    </>
  )
}

export default ReviewStep
