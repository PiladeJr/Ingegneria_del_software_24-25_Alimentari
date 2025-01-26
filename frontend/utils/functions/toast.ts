const toastComponent = useToast();

const toast = (message: string, error: boolean) => {
  toastComponent.add({
    id: error ? "error_" + message : "success_" + message,
    title: message,
    icon: error ? "i-heroicons-x-circle" : "i-heroicons-check-circle",
    color: error ? "red" : "green",
  });
};

export { toast };
