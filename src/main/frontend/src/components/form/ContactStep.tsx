/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
import { useFormContext, Controller } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import {
  FormControl,
  FormHelperText,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
} from '@mui/material'
import { FnolFormData } from '../../pages/FnolFormPage'

const countries = ['UAE', 'SAU', 'KWT', 'QAT', 'BHR', 'OMN']

function ContactStep() {
  const { t } = useTranslation()
  const {
    control,
    formState: { errors },
  } = useFormContext<FnolFormData>()

  return (
    <>
      <Typography variant="h6" gutterBottom>
        {t('form.contact.title')}
      </Typography>

      <Grid container spacing={3}>
        <Grid item xs={12} sm={6}>
          <Controller
            name="countryCode"
            control={control}
            rules={{ required: t('form.validation.required') }}
            render={({ field }) => (
              <FormControl fullWidth error={!!errors.countryCode}>
                <InputLabel>{t('form.contact.country')}</InputLabel>
                <Select {...field} label={t('form.contact.country')}>
                  {countries.map((code) => (
                    <MenuItem key={code} value={code}>
                      {t(`countries.${code}`)}
                    </MenuItem>
                  ))}
                </Select>
                {errors.countryCode && (
                  <FormHelperText>{errors.countryCode.message}</FormHelperText>
                )}
              </FormControl>
            )}
          />
        </Grid>

        <Grid item xs={12} sm={6}>
          <Controller
            name="mobileNumber"
            control={control}
            rules={{ required: t('form.validation.required') }}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('form.contact.mobileNumber')}
                error={!!errors.mobileNumber}
                helperText={errors.mobileNumber?.message}
                placeholder="+971501234567"
              />
            )}
          />
        </Grid>

        <Grid item xs={12} sm={6}>
          <Controller
            name="nationalId"
            control={control}
            rules={{ required: t('form.validation.required') }}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('form.contact.nationalId')}
                error={!!errors.nationalId}
                helperText={errors.nationalId?.message}
              />
            )}
          />
        </Grid>

        <Grid item xs={12} sm={6}>
          <Controller
            name="reporterName"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('form.contact.name')}
                error={!!errors.reporterName}
                helperText={errors.reporterName?.message}
              />
            )}
          />
        </Grid>

        <Grid item xs={12}>
          <Controller
            name="reporterEmail"
            control={control}
            rules={{
              pattern: {
                value: /^[^\s@]+@[^\s@]+\.[^\s@]+$/,
                message: t('form.validation.invalidEmail'),
              },
            }}
            render={({ field }) => (
              <TextField
                {...field}
                type="email"
                label={t('form.contact.email')}
                error={!!errors.reporterEmail}
                helperText={errors.reporterEmail?.message}
              />
            )}
          />
        </Grid>
      </Grid>
    </>
  )
}

export default ContactStep
