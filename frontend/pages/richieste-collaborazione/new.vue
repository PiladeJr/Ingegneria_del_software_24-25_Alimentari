<template>
  <div>
    <div>Nuova Richiesta Collaborazione</div>
    <div class="flex items-center justify-center w-full">
      <div class="w-full lg:w-1/3 p-4 rounded-md shadow-lg bg-gray-200">
        <UProgress :value="step" :max="5" />
        <p class="text-sm mt-2 mb-4">Step {{ step }} di 5</p>
        <div v-if="step === 1">
          <div class="text-lg font-bold">Seleziona Un Ruolo</div>
          <div class="flex flex-col gap-y-4">
            <UFormGroup label="Ruolo">
              <USelect
                v-model="richiestaCollaborazione.ruolo"
                label="Ruolo"
                :options="roles"
              />
            </UFormGroup>
          </div>
        </div>
        <div v-if="step === 2">
          <div class="text-lg mt-6 font-bold">Dati Anagrafici</div>
          <div class="grid grid-cols-2 gap-4">
            <UFormGroup label="Nome">
              <UInput v-model="richiestaCollaborazione.nome" label="Nome" />
            </UFormGroup>
            <UFormGroup label="Cognome">
              <UInput
                v-model="richiestaCollaborazione.cognome"
                label="Cognome"
              />
            </UFormGroup>
            <UFormGroup label="Email">
              <UInput
                type="email"
                v-model="richiestaCollaborazione.email"
                label="Email"
              />
            </UFormGroup>
            <UFormGroup label="Telefono">
              <UInput
                v-model="richiestaCollaborazione.telefono"
                label="Telefono"
              />
            </UFormGroup>
          </div>
        </div>
        <div v-if="step === 3">
          <div class="text-lg mt-6 font-bold">Dati Azienda</div>
          <div class="grid grid-cols-2 gap-4">
            <UFormGroup label="Azienda Referente">
              <UInput
                v-model="richiestaCollaborazione.aziendaReferente"
                label="Azienda Referente"
              />
            </UFormGroup>
            <UFormGroup label="IBAN">
              <UInput v-model="richiestaCollaborazione.iban" label="IBAN" />
            </UFormGroup>
            <UFormGroup label="Partita IVA">
              <UInput
                v-model="richiestaCollaborazione.iva"
                label="Partita IVA"
              />
            </UFormGroup>
            <UFormGroup label="Denominazione Sociale">
              <UInput
                v-model="richiestaCollaborazione.denSociale"
                label="Denominazione Sociale"
              />
            </UFormGroup>
            <UFormGroup label="Sede Legale" class="col-span-2">
              <div class="grid grid-cols-2 gap-2">
                <UInput
                  v-model="richiestaCollaborazione.sedeLegale.via"
                  placeholder="Via"
                />
                <UInput
                  v-model="richiestaCollaborazione.sedeLegale.numeroCivico"
                  placeholder="Numero Civico"
                />
                <UInput
                  v-model="richiestaCollaborazione.sedeLegale.cap"
                  placeholder="CAP"
                />
                <UInput
                  v-model="richiestaCollaborazione.sedeLegale.citta"
                  placeholder="Città"
                />
                <UInput
                  v-model="richiestaCollaborazione.sedeLegale.provincia"
                  placeholder="Provincia"
                  maxlength="2"
                />
              </div>
            </UFormGroup>
            <UFormGroup label="Sede Operativa (Opzionale)" class="col-span-2">
              <div class="grid grid-cols-2 gap-2">
                <UInput
                  v-model="richiestaCollaborazione.sedeOperativa.via"
                  placeholder="Via"
                />
                <UInput
                  v-model="richiestaCollaborazione.sedeOperativa.numeroCivico"
                  placeholder="Numero Civico"
                />
                <UInput
                  v-model="richiestaCollaborazione.sedeOperativa.cap"
                  placeholder="CAP"
                />
                <UInput
                  v-model="richiestaCollaborazione.sedeOperativa.citta"
                  placeholder="Città"
                />
                <UInput
                  v-model="richiestaCollaborazione.sedeOperativa.provincia"
                  placeholder="Provincia"
                />
              </div>
            </UFormGroup>
            <UFormGroup label="Certificato">
              <FileInput
                accept="application/pdf"
                @file-selected="richiestaCollaborazione.certificato = $event"
              />
            </UFormGroup>
          </div>
        </div>
        <div v-if="step === 4">
          <div class="text-lg mt-6 font-bold">Riepilogo Dati</div>
          <div class="grid grid-cols-2 gap-4">
            <div><strong>Nome:</strong> {{ richiestaCollaborazione.nome }}</div>
            <div>
              <strong>Cognome:</strong> {{ richiestaCollaborazione.cognome }}
            </div>
            <div>
              <strong>Email:</strong> {{ richiestaCollaborazione.email }}
            </div>
            <div>
              <strong>Telefono:</strong> {{ richiestaCollaborazione.telefono }}
            </div>
            <div>
              <strong>Ruolo:</strong> {{ richiestaCollaborazione.ruolo }}
            </div>
            <div>
              <strong>Azienda Referente:</strong>
              {{ richiestaCollaborazione.aziendaReferente }}
            </div>
            <div><strong>IBAN:</strong> {{ richiestaCollaborazione.iban }}</div>
            <div>
              <strong>Partita IVA:</strong> {{ richiestaCollaborazione.iva }}
            </div>
            <div>
              <strong>Denominazione Sociale:</strong>
              {{ richiestaCollaborazione.denSociale }}
            </div>
            <div class="col-span-2">
              <strong>Sede Legale:</strong>
              {{ richiestaCollaborazione.sedeLegale.via }},
              {{ richiestaCollaborazione.sedeLegale.numeroCivico }},
              {{ richiestaCollaborazione.sedeLegale.cap }},
              {{ richiestaCollaborazione.sedeLegale.citta }},
              {{ richiestaCollaborazione.sedeLegale.provincia }}
            </div>
            <div class="col-span-2">
              <strong>Sede Operativa:</strong>
              {{ richiestaCollaborazione.sedeOperativa.via }},
              {{ richiestaCollaborazione.sedeOperativa.numeroCivico }},
              {{ richiestaCollaborazione.sedeOperativa.cap }},
              {{ richiestaCollaborazione.sedeOperativa.citta }},
              {{ richiestaCollaborazione.sedeOperativa.provincia }}
            </div>
            <div>
              <strong>Certificato:</strong>
              {{
                richiestaCollaborazione.certificato
                  ? richiestaCollaborazione.certificato.name
                  : "Non caricato"
              }}
            </div>
          </div>
        </div>
        <div class="flex justify-between mt-4">
          <div v-if="step === 1"></div>
          <UButton v-if="step > 1" @click="step--">Indietro</UButton>
          <UButton
            @click="step < 5 ? step++ : createRichiestaCollaborazioneAzienda()"
            >Avanti</UButton
          >
        </div>
      </div>
    </div>
  </div>
</template>
<script setup lang="ts">
type Indirizzo = {
  citta: string;
  cap: string;
  via: string;
  numeroCivico: string;
  provincia: string;
  coordinate: string;
};

const step = ref(1);
const roles = [
  "CURATORE",
  "ANIMATORE",
  "PRODUTTORE",
  "TRASFORMATORE",
  "DISTRIBUTORE",
];

const sedeLegaleIndirizzo = ref();
const sedeOperativaIndirizzo = ref();
const toast = useToast();
const richiestaCollaborazione = reactive({
  nome: "",
  cognome: "",
  email: "",
  telefono: "",
  ruolo: "",
  aziendaReferente: "",
  iban: "",
  iva: "",
  denSociale: "",
  sedeLegale: {
    citta: "",
    cap: "",
    via: "",
    numeroCivico: "",
    provincia: "",
    coordinate: "",
  } as Indirizzo,
  sedeOperativa: {
    citta: "",
    cap: "",
    via: "",
    numeroCivico: "",
    provincia: "",
    coordinate: "",
  } as Indirizzo,
  cartaIdentita: undefined,
  cv: undefined,
  certificato: undefined as any,
});

const geoCoding = async (address: string) => {
  const response = await fetch(
    `  https://nominatim.openstreetmap.org/search?q=${address}&format=json`
  );
  const data = await response.json();
  return data;
};
const loading = ref(false);
const createRichiestaCollaborazioneAzienda = async () => {
  loading.value = true;
  try {
    const data =
      await useNuxtApp().$api.richieste_collaborazione.createRichiestaCollaborazioneAzienda(
        richiestaCollaborazione
      );
    loading.value = false;
    toast.add({
      id: "success_create_richiesta_collaborazione",
      title: "Richiesta di collaborazione creata con successo",
      icon: "i-heroicons-check-circle",
      color: "green",
    });
  } catch (error: { response: string } | any) {
    loading.value = false;
    toast.add({
      id: "error_get_richieste_collaborazione",
      title: error.response._data.error,
      icon: "i-heroicons-x-circle",
      color: "red",
    });
  }
};

watch(
  () => step.value,
  async (newStep) => {
    if (newStep === 4) {
      const address = `${richiestaCollaborazione.sedeLegale.via}, ${richiestaCollaborazione.sedeLegale.numeroCivico}, ${richiestaCollaborazione.sedeLegale.cap}, ${richiestaCollaborazione.sedeLegale.citta}, ${richiestaCollaborazione.sedeLegale.provincia}`;
      const data = await geoCoding(address);
      if (data.length > 0) {
        richiestaCollaborazione.sedeLegale.coordinate = `${data[0].lat},${data[0].lon}`;
      }
    }
  }
);
</script>
