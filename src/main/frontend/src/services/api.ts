/*
 * GCC Motor FNOL Starter Kit
 * Copyright 2025 G. Ganesh Kumar | Solution Architect
 * Contact: UAE +971-55-816-0396 | WhatsApp +91-95000-03051
 * Apache License 2.0 - https://www.apache.org/licenses/LICENSE-2.0
 */
import axios from 'axios'

const api = axios.create({
  baseURL: '/api/v1',
  headers: {
    'Content-Type': 'application/json',
  },
})

export interface FnolSubmitRequest {
  countryCode: string
  mobileNumber: string
  nationalId: string
  reporterName?: string
  reporterEmail?: string
  plateNumber: string
  plateCountry?: string
  vehicleType?: string
  vehicleMake?: string
  vehicleModel?: string
  vehicleYear?: number | null
  vehicleColor?: string
  policyNumber?: string
  coverageType?: string
  isFleet?: boolean
  incidentDate: string
  incidentTime?: string
  incidentLocation?: string
  latitude?: number | null
  longitude?: number | null
  description?: string
  isDrivable?: boolean
  hasInjuries?: boolean
  thirdPartyInvolved?: boolean
  policeReportNumber?: string
  preferredLanguage?: string
  attachments?: Array<{ url: string; type: string; description: string }>
}

export interface FnolSubmitResponse {
  fnolId: string
  status: string
  severityLevel: string
  route: string
  processInstanceKey: string
  createdAt: string
  isDuplicate: boolean
  message: string
}

export interface FnolStatusResponse {
  fnolId: string
  status: string
  severityLevel: string
  route: string
  createdAt: string
  updatedAt: string
}

export interface FnolDetailResponse extends FnolStatusResponse {
  countryCode: string
  mobileNumber: string
  nationalId: string
  reporterName: string
  reporterEmail: string
  plateNumber: string
  vehicleType: string
  vehicleMake: string
  vehicleModel: string
  vehicleYear: number
  vehicleColor: string
  policyNumber: string
  coverageType: string
  isFleet: boolean
  incidentDate: string
  incidentTime: string
  incidentLocation: string
  latitude: number
  longitude: number
  description: string
  isDrivable: boolean
  hasInjuries: boolean
  thirdPartyInvolved: boolean
  policeReportNumber: string
  preferredLanguage: string
  processInstanceKey: string
  attachments: Array<{ url: string; type: string; description: string }>
}

export async function submitFnol(
  request: FnolSubmitRequest,
  idempotencyKey?: string
): Promise<FnolSubmitResponse> {
  const headers: Record<string, string> = {}
  if (idempotencyKey) {
    headers['X-Idempotency-Key'] = idempotencyKey
  }

  const response = await api.post<FnolSubmitResponse>('/fnol', request, { headers })
  return response.data
}

export async function getFnolStatus(fnolId: string): Promise<FnolStatusResponse> {
  const response = await api.get<FnolStatusResponse>(`/fnol/${fnolId}/status`)
  return response.data
}

export async function getFnolDetail(fnolId: string): Promise<FnolDetailResponse> {
  const response = await api.get<FnolDetailResponse>(`/fnol/${fnolId}`)
  return response.data
}

export async function getAppInfo(): Promise<{
  application: string
  version: string
  demoMode: boolean
  supportedCountries: string[]
}> {
  const response = await api.get('/info')
  return response.data
}

export default api
