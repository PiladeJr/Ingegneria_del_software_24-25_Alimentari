export const buildMultiPartFormData = (
  data: Record<string, any>,
  formData: FormData = new FormData(),
  parentKey: string | null = null
) => {
  for (const key in data) {
    const value = data[key];
    const formKey = parentKey ? `${parentKey}.${key}` : key;

    if (value instanceof Array) {
      value.forEach((item, index) => {
        if (item instanceof File) {
          formData.append(formKey, item); // Append without [0]
          console.log("file", item + formKey);
        } else {
          const arrayKey = `${formKey}[${index}]`;
          if (typeof item === "object") {
            buildMultiPartFormData(item, formData, arrayKey);
          } else {
            formData.append(arrayKey, item);
          }
        }
      });
    } else if (typeof value === "object" && !(value instanceof File)) {
      buildMultiPartFormData(value, formData, formKey);
    } else {
      formData.append(formKey, value);
    }
  }
  return formData;
};
