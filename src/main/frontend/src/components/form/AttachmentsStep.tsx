/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
import { useFormContext, useFieldArray } from 'react-hook-form'
import { useTranslation } from 'react-i18next'
import {
  Box,
  Button,
  Card,
  CardContent,
  Grid,
  IconButton,
  TextField,
  Typography,
  FormControl,
  InputLabel,
  Select,
  MenuItem,
} from '@mui/material'
import AddPhotoAlternateIcon from '@mui/icons-material/AddPhotoAlternate'
import DeleteIcon from '@mui/icons-material/Delete'
import { FnolFormData } from '../../pages/FnolFormPage'

const attachmentTypes = ['IMAGE', 'VIDEO', 'DOCUMENT', 'AUDIO']

function AttachmentsStep() {
  const { t } = useTranslation()
  const { control, register } = useFormContext<FnolFormData>()
  const { fields, append, remove } = useFieldArray({
    control,
    name: 'attachments',
  })

  const addAttachment = () => {
    append({ url: '', type: 'IMAGE', description: '' })
  }

  return (
    <>
      <Typography variant="h6" gutterBottom>
        {t('form.attachments.title')}
      </Typography>

      <Box sx={{ mb: 3 }}>
        <Button
          variant="outlined"
          startIcon={<AddPhotoAlternateIcon />}
          onClick={addAttachment}
        >
          Add Attachment URL
        </Button>
      </Box>

      {fields.length === 0 ? (
        <Typography color="text.secondary" sx={{ py: 4, textAlign: 'center' }}>
          {t('form.attachments.noAttachments')}
        </Typography>
      ) : (
        <Grid container spacing={2}>
          {fields.map((field, index) => (
            <Grid item xs={12} key={field.id}>
              <Card variant="outlined">
                <CardContent>
                  <Box sx={{ display: 'flex', alignItems: 'flex-start', gap: 2 }}>
                    <Box sx={{ flex: 1 }}>
                      <Grid container spacing={2}>
                        <Grid item xs={12} sm={6}>
                          <TextField
                            {...register(`attachments.${index}.url`)}
                            label="URL"
                            placeholder="https://..."
                            size="small"
                            fullWidth
                          />
                        </Grid>
                        <Grid item xs={12} sm={3}>
                          <FormControl fullWidth size="small">
                            <InputLabel>Type</InputLabel>
                            <Select
                              {...register(`attachments.${index}.type`)}
                              label="Type"
                              defaultValue={field.type}
                            >
                              {attachmentTypes.map((type) => (
                                <MenuItem key={type} value={type}>
                                  {type}
                                </MenuItem>
                              ))}
                            </Select>
                          </FormControl>
                        </Grid>
                        <Grid item xs={12} sm={3}>
                          <TextField
                            {...register(`attachments.${index}.description`)}
                            label="Description"
                            size="small"
                            fullWidth
                          />
                        </Grid>
                      </Grid>
                    </Box>
                    <IconButton
                      color="error"
                      onClick={() => remove(index)}
                      sx={{ mt: 0.5 }}
                    >
                      <DeleteIcon />
                    </IconButton>
                  </Box>
                </CardContent>
              </Card>
            </Grid>
          ))}
        </Grid>
      )}

      <Typography variant="body2" color="text.secondary" sx={{ mt: 3 }}>
        Note: Enter URLs to photos, videos, or documents. In production, this would integrate with
        a file upload service.
      </Typography>
    </>
  )
}

export default AttachmentsStep
