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
  FormControl,
  FormControlLabel,
  Grid,
  InputLabel,
  MenuItem,
  Select,
  TextField,
  Typography,
} from '@mui/material'
import { FnolFormData } from '../../pages/FnolFormPage'

const countries = ['UAE', 'SAU', 'KWT', 'QAT', 'BHR', 'OMN']
const vehicleTypes = ['SEDAN', 'SUV', 'PICKUP', 'VAN', 'TRUCK', 'MOTORCYCLE', 'BUS', 'OTHER']
const coverageTypes = ['COMPREHENSIVE', 'TPL']

function VehicleStep() {
  const { t } = useTranslation()
  const {
    control,
    formState: { errors },
  } = useFormContext<FnolFormData>()

  return (
    <>
      <Typography variant="h6" gutterBottom>
        {t('form.vehicle.title')}
      </Typography>

      <Grid container spacing={3}>
        <Grid item xs={12} sm={6}>
          <Controller
            name="plateNumber"
            control={control}
            rules={{ required: t('form.validation.required') }}
            render={({ field }) => (
              <TextField
                {...field}
                label={t('form.vehicle.plateNumber')}
                error={!!errors.plateNumber}
                helperText={errors.plateNumber?.message}
              />
            )}
          />
        </Grid>

        <Grid item xs={12} sm={6}>
          <Controller
            name="plateCountry"
            control={control}
            render={({ field }) => (
              <FormControl fullWidth>
                <InputLabel>{t('form.vehicle.plateCountry')}</InputLabel>
                <Select {...field} label={t('form.vehicle.plateCountry')}>
                  {countries.map((code) => (
                    <MenuItem key={code} value={code}>
                      {t(`countries.${code}`)}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            )}
          />
        </Grid>

        <Grid item xs={12} sm={6}>
          <Controller
            name="vehicleType"
            control={control}
            render={({ field }) => (
              <FormControl fullWidth>
                <InputLabel>{t('form.vehicle.vehicleType')}</InputLabel>
                <Select {...field} label={t('form.vehicle.vehicleType')}>
                  {vehicleTypes.map((type) => (
                    <MenuItem key={type} value={type}>
                      {t(`vehicleTypes.${type}`)}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            )}
          />
        </Grid>

        <Grid item xs={12} sm={6}>
          <Controller
            name="coverageType"
            control={control}
            render={({ field }) => (
              <FormControl fullWidth>
                <InputLabel>{t('form.vehicle.coverageType')}</InputLabel>
                <Select {...field} label={t('form.vehicle.coverageType')}>
                  {coverageTypes.map((type) => (
                    <MenuItem key={type} value={type}>
                      {t(`coverageTypes.${type}`)}
                    </MenuItem>
                  ))}
                </Select>
              </FormControl>
            )}
          />
        </Grid>

        <Grid item xs={12} sm={4}>
          <Controller
            name="vehicleMake"
            control={control}
            render={({ field }) => (
              <TextField {...field} label={t('form.vehicle.make')} />
            )}
          />
        </Grid>

        <Grid item xs={12} sm={4}>
          <Controller
            name="vehicleModel"
            control={control}
            render={({ field }) => (
              <TextField {...field} label={t('form.vehicle.model')} />
            )}
          />
        </Grid>

        <Grid item xs={12} sm={4}>
          <Controller
            name="vehicleYear"
            control={control}
            render={({ field }) => (
              <TextField
                {...field}
                type="number"
                label={t('form.vehicle.year')}
                onChange={(e) => field.onChange(e.target.value ? parseInt(e.target.value) : null)}
              />
            )}
          />
        </Grid>

        <Grid item xs={12} sm={6}>
          <Controller
            name="vehicleColor"
            control={control}
            render={({ field }) => (
              <TextField {...field} label={t('form.vehicle.color')} />
            )}
          />
        </Grid>

        <Grid item xs={12} sm={6}>
          <Controller
            name="policyNumber"
            control={control}
            render={({ field }) => (
              <TextField {...field} label={t('form.vehicle.policyNumber')} />
            )}
          />
        </Grid>

        <Grid item xs={12}>
          <Controller
            name="isFleet"
            control={control}
            render={({ field }) => (
              <FormControlLabel
                control={<Checkbox {...field} checked={field.value} />}
                label={t('form.vehicle.isFleet')}
              />
            )}
          />
        </Grid>
      </Grid>
    </>
  )
}

export default VehicleStep
