/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
import { useFormContext, Controller } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import {
  Checkbox,
  FormControlLabel,
  Grid,
  TextField,
  Typography,
} from '@mui/material'
import { FnolFormData } from '../../pages/FnolFormPage'

function IncidentStep() {
  const { t } = useTranslation()
  const {
    control,
    formState: { errors },
  } = useFormContext<FnolFormData>()

  return (
    <>
      <Typography variant="h6" gutterBottom>
        {t('form.incident.title')}
      </Typography>

      <Grid container spacing={3}>
        <Grid item xs={12} sm={6}>
          <Controller
            name="incidentDate"
            control={control}
            rules={{ required: t('form.validation.required') }}
            render={({ field }) => (
              <TextField
                {...field}
                type="date"
                label={t('form.incident.date')}
                InputLabelProps={{ shrink: true }}
                error={!!errors.incidentDate}
                helperText={errors.incidentDate?.message}
              />
            )}
          />
        </Grid>

        <Grid item xs={12} sm={6}>
          <Controller
            name="incidentTime"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                type="time"
                label={t('form.incident.time')}
                InputLabelProps={{ shrink: true }}
              />
            )}
          />
        </Grid>

        <Grid item xs={12}>
          <Controller
            name="incidentLocation"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('form.incident.location')}
                placeholder="Enter address or landmark"
              />
            )}
          />
        </Grid>

        <Grid item xs={12}>
          <Controller
            name="description"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                multiline
                rows={4}
                label={t('form.incident.description')}
                placeholder="Please describe what happened..."
              />
            )}
          />
        </Grid>

        <Grid item xs={12} sm={6}>
          <Controller
            name="policeReportNumber"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('form.incident.policeReport')}
              />
            )}
          />
        </Grid>

        <Grid item xs={12}>
          <Controller
            name="isDrivable"
            control={control}
            render={({ field }) => (
              <FormControlLabel
                control={<Checkbox {...field} checked={field.value} />}
                label={t('form.incident.isDrivable')}
              />
            )}
          />
        </Grid>

        <Grid item xs={12}>
          <Controller
            name="hasInjuries"
            control={control}
            render={({ field }) => (
              <FormControlLabel
                control={<Checkbox {...field} checked={field.value} color="error" />}
                label={t('form.incident.hasInjuries')}
              />
            )}
          />
        </Grid>

        <Grid item xs={12}>
          <Controller
            name="thirdPartyInvolved"
            control={control}
            render={({ field }) => (
              <FormControlLabel
                control={<Checkbox {...field} checked={field.value} />}
                label={t('form.incident.thirdParty')}
              />
            )}
          />
        </Grid>
      </Grid>
    </>
  )
}

export default IncidentStep
