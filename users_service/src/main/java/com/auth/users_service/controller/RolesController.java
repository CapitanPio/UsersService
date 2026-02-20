package com.auth.users_service.controller;


@RestController
@RequestMapping("/api/auth")
@ConditionalOnProperty(prefix = "roles", name = "allowed", havingValue = "true")
public class RolesController {
    
    private final RolesService rolesService;

    public RolesController(RolesService rolesService) {
        this.rolesService = rolesService;
    }

    @PostMapping("/roles")
    public ResponseEntity<CreateRoleResponse> createRole(@RequestBody CreateRoleRequest request) {
        CreateRoleResponse response = rolesService.createRole(request);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/roles/{name}")
    public ResponseEntity<ReadRoleResponse> readRole(@PathVariable String name) {
        ReadRoleResponse response = rolesService.readRole(new ReadRoleRequest(name));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/roles")
    public ResponseEntity<List<ReadRoleResponse>> readAllRoles() {
        List<ReadRoleResponse> response = rolesService.readAllRoles();
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/roles/{name}")
    public ResponseEntity<DeleteRoleResponse> deleteRole(@PathVariable String name) {
        DeleteRoleResponse response = rolesService.deleteRole(new DeleteRoleRequest(name));
        return ResponseEntity.ok(response);
    }

    @PutMapping("/roles")
    public ResponseEntity<EditRoleResponse> editRole(@RequestBody EditRoleRequest request) {
        EditRoleResponse response = rolesService.editRole(request);
        return ResponseEntity.ok(response);
    }
    
}
