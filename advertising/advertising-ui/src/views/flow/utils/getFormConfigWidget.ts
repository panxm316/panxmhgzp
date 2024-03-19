const gets = {} as any
const modules = import.meta.glob('../form/config/*.vue', { eager: true }) as any

for (const each in modules) {
  const name = modules[each].default.__name
  gets[name] = (modules[each] as any).default
}

export default gets
