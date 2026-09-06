
## Decisiones de diseño

### Necesidad 1 — Aprobación por niveles jerárquicos

**Patrón aplicado:** Chain of Responsibility

**Justificación:** El problema central es que una solicitud debe recorrer una
secuencia de decisores independientes (Supervisor de Área, Gerente de Área,
Director Financiero, y opcionalmente el Revisor de Cumplimiento Normativo para
categoría INTERNACIONAL), donde cada uno decide si resuelve la solicitud según
el monto o la delega al siguiente nivel. Esa secuencia debe poder crecer o
reordenarse sin que `ControladorSolicitudes` conozca cuántos niveles existen ni
en qué orden se consultan. Cada `NivelAprobacion` decide si `puedeResolver()` y,
si no, delega al `siguiente` configurado — agregar el nivel de Cumplimiento
Normativo solo requirió insertarlo al inicio de la cadena, sin tocar los demás
niveles ni el controlador.

Se descartó Command porque Command encapsula una operación reversible que
alguien ejecuta y puede deshacer después; aquí no hay ninguna operación que
deshacer, sino una petición que avanza por una cadena hasta que alguien la
resuelve — un problema estructuralmente distinto, aunque ambos patrones
aparezcan en este mismo laboratorio (ver Necesidad 2).

### Necesidad 2 — Ejecución reversible de solicitudes

**Patrón aplicado:** Command

**Justificación:** Reservar presupuesto y generar orden de compra son operaciones
discretas que deben poder ejecutarse y deshacerse de forma independiente, con un
historial ordenado y consultable de todas las operaciones realizadas sobre una
solicitud (no solo la última). `ReservarPresupuestoCommand` y
`GenerarOrdenCompraCommand` encapsulan cada operación como un objeto con
`ejecutar()` y `deshacer()`, y `EjecutorSolicitud` mantiene ese historial en una
lista, permitiendo deshacer cualquiera de ellas de forma independiente sin
modificar `PresupuestoService` ni `OrdenCompraService`.

Se descartó Chain of Responsibility porque aquí no hay ningún decisor evaluando
condiciones para decidir si delega o resuelve una petición entrante; hay
operaciones discretas que un mismo actor (el equipo de Compras) decide ejecutar
y, eventualmente, deshacer, y que deben quedar registradas en orden para poder
inspeccionarse — algo que Chain of Responsibility no modela, ya que no tiene
noción de reversibilidad ni de historial.

### Necesidad 3 — Notificaciones ante cambio de estado

**Patrón aplicado:** Observer

**Justificación:** Cada vez que una solicitud cambia de estado —ya sea por la
evaluación de la Necesidad 1 o por la ejecución de la Necesidad 2— deben
dispararse tres reacciones independientes (correo, dashboard, auditoría) sin que
el código que cambia el estado conozca esas tres reacciones directamente.
`NotificadorCambioEstado` actúa como sujeto: mantiene una lista de
`ObservadorCambioEstado` suscritos y los notifica ante cada cambio, sin conocer
qué hace cada uno. Agregar una cuarta reacción (demostrado en el test con un
colector de prueba) solo requiere llamar a `suscribir()`, sin modificar
`NotificadorCambioEstado` ni los puntos donde se dispara la notificación.

Se descartó el patrón de la Necesidad 4 (State) porque State modela el
comportamiento *propio* de un objeto según su estado interno —qué operaciones
puede realizar la Solicitud según en qué estado se encuentra—; aquí el problema
es distinto: son objetos completamente ajenos a la Solicitud (correo,
contabilidad, auditoría) quienes deben enterarse y reaccionar cuando ella ya
cambió, sin que la Solicitud ni el código que la modifica necesiten conocerlos.

### Necesidad 4 — Reglas de transición según el estado

**Patrón aplicado:** State

**Justificación:** Las reglas sobre qué operaciones son válidas (aprobar,
rechazar, ejecutar, cancelar) estaban dispersas como if/else repetidos,
revisando `getEstado()` en cada método, y agregar un estado nuevo obligaba a
tocar varios métodos a la vez. Cada estado (`EstadoPendiente`, `EstadoAprobada`,
`EstadoEjecutada`, `EstadoRechazada`, `EstadoCancelada`) implementa
`EstadoSolicitud` y decide qué operaciones acepta y a qué estado transiciona;
`ContextoSolicitud` delega cada operación en el estado actual, y agregar un
estado nuevo (como el `EN_ESPERA_PROVEEDOR` planeado) sería agregar una clase
nueva, no modificar if/else existentes.

Se descartó Strategy, visto en la guía con una estructura parecida (un cliente
externo que elige e inyecta el comportamiento activo, como un carrito que activa
la estrategia de descuento que desea usar). Aquí no hay un cliente externo
seleccionando entre comportamientos intercambiables en cada llamada: es la
propia solicitud quien decide qué operaciones son válidas según en qué estado se
encuentra en ese instante de su historia, y además debe poder transicionar de un
estado a otro como parte de resolver la operación — algo que un conjunto de
estrategias independientes entre sí no hace por su cuenta, ya que las estrategias
no se "convierten" unas en otras.

### Reflexión — otros tres patrones (no rubricada)

**Iterator:** un reporte que recorre secuencialmente todas las solicitudes de un
centro de costo sin exponer si están almacenadas en una lista, un mapa o una
estructura distinta encajaría naturalmente con Iterator, que expone una forma
uniforme de recorrer una colección sin revelar su representación interna.

**Template Method:** los tres tipos de comprobante (orden de compra, comprobante
de reserva presupuestal, acta de rechazo) que comparten el mismo esqueleto de
impresión (encabezado, cuerpo, pie) pero difieren solo en cómo llenan el cuerpo
encajarían con Template Method, que fija el esqueleto del algoritmo en una clase
base y delega los pasos variables a las subclases.

**Memento:** guardar y restaurar instantáneas completas del estado de una
solicitud en cualquier punto de su historia, sin que el código que las guarda
conozca los detalles internos de `Solicitud`, se acercaría a Memento. Se
diferencia de lo construido en la Necesidad 2 en que Command encapsula
*operaciones* reversibles (acciones con su propia lógica de deshacer), mientras
que Memento encapsula *estados* completos capturados en un momento dado, sin
lógica de negocio asociada al cómo se llegó a ese estado.

## Herramientas utilizadas

- Java 17, Spring Boot 3.2, Apache Maven, JUnit 5
- VS Code, Git, GitHub

## Estructura del proyecto

```
src/main/java/com/universidad/compras/
├── ComprasApp.java
├── modelo/
│   └── Solicitud.java              (dado — entidad compartida)
├── aprobacion/                      (Necesidad 1 — Chain of Responsibility)
│   ├── ServicioAprobacion.java      (dado)
│   ├── ResultadoAprobacion.java     (dado)
│   ├── ControladorSolicitudes.java  (dado)
│   ├── NivelAprobacion.java
│   ├── NivelSupervisorArea.java
│   ├── NivelGerenteArea.java
│   ├── NivelDirectorFinanciero.java
│   ├── NivelCumplimientoNormativo.java
│   └── ServicioAprobacionPorNiveles.java
├── ejecucion/                       (Necesidad 2 — Command)
│   ├── PresupuestoService.java      (dado)
│   ├── OrdenCompraService.java      (dado)
│   ├── OperacionEjecutable.java
│   ├── ReservarPresupuestoCommand.java
│   ├── GenerarOrdenCompraCommand.java
│   └── EjecutorSolicitud.java
├── notificacion/                    (Necesidad 3 — Observer)
│   ├── ClientesNotificacion.java    (dado)
│   ├── ObservadorCambioEstado.java
│   ├── NotificacionCorreoObserver.java
│   ├── NotificacionDashboardObserver.java
│   ├── NotificacionAuditoriaObserver.java
│   └── NotificadorCambioEstado.java
└── estado/                          (Necesidad 4 — State)
    ├── EstadoSolicitud.java
    ├── EstadoPendiente.java
    ├── EstadoAprobada.java
    ├── EstadoEjecutada.java
    ├── EstadoRechazada.java
    ├── EstadoCancelada.java
    └── ContextoSolicitud.java
```

## Conclusiones

Este post-contenido mostró que dos patrones pueden compartir el mismo dominio de
problema y aun así ser completamente distintos según el síntoma exacto que
resuelven: Chain of Responsibility modela una petición que avanza por una
secuencia de decisores hasta que alguien la resuelve, mientras que Command
modela operaciones discretas y reversibles ejecutadas por un mismo actor. De
forma similar, Observer y State comparten el contexto de "una Solicitud que
cambia", pero Observer resuelve la reacción de módulos externos ajenos al objeto
que cambió, mientras que State resuelve el comportamiento propio del objeto
según su estado interno. La comparación más sutil fue distinguir State de
Strategy: ambos delegan comportamiento en un objeto colaborador, pero solo State
permite que ese colaborador cambie a otro como parte de resolver la operación,
algo que un conjunto de estrategias intercambiables elegidas por un cliente
externo no hace por sí mismo. La dificultad principal no fue implementar cada
patrón, sino identificar con precisión cuál síntoma correspondía a cuál patrón
antes de escribir código.