import consts from "@/helpers/consts";

export default function checkUserPermissions(user) {
    let permissionRoutes
    let permissionEmployees
    let permissionRoles
    let permissionStops
    switch (user.role.id) {
        // Диспетчер
        case 1:
            switch (user.role.classification) {
                // Стажёр
                case 1:
                    permissionRoutes = consts.PERMISSIONS.READ
                    permissionEmployees = consts.PERMISSIONS.READ
                    permissionRoles = consts.PERMISSIONS.READ
                    permissionStops = consts.PERMISSIONS.READ
                    break
                // Младший
                case 2:
                    permissionRoutes = consts.PERMISSIONS.WRITE
                    permissionStops = consts.PERMISSIONS.WRITE
                    permissionEmployees = consts.PERMISSIONS.READ
                    permissionRoles = consts.PERMISSIONS.READ
                    break
                // Старший
                case 3:
                    permissionRoutes = consts.PERMISSIONS.WRITE
                    permissionStops = consts.PERMISSIONS.WRITE
                    permissionEmployees = consts.PERMISSIONS.WRITE
                    permissionRoles = consts.PERMISSIONS.WRITE
                    break

            }
            break
        // Машинист
        case 2:
            switch (user.role.classification) {
                // Стажёр
                case 1:
                    permissionRoutes = consts.PERMISSIONS.READ
                    permissionStops = consts.PERMISSIONS.READ
                    permissionEmployees = consts.PERMISSIONS.READ
                    permissionRoles = consts.PERMISSIONS.READ
                    break
                // Младший
                case 2:
                    permissionRoutes = consts.PERMISSIONS.READ
                    permissionStops = consts.PERMISSIONS.WRITE
                    permissionEmployees = consts.PERMISSIONS.READ
                    permissionRoles = consts.PERMISSIONS.READ
                    break
                // Старший
                case 3:
                    permissionRoutes = consts.PERMISSIONS.WRITE
                    permissionStops = consts.PERMISSIONS.WRITE
                    permissionEmployees = consts.PERMISSIONS.READ
                    permissionRoles = consts.PERMISSIONS.READ
                    break
            }
        break
    }
    return Object.assign({}, {routes: permissionRoutes, stops: permissionStops, employees: permissionEmployees, roles: permissionRoles})
}