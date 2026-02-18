<script setup lang="ts"></script>

<template>
  <div class="modal show d-block" tabindex="-1">
    <div class="modal-dialog">
      <div class="modal-content">
        <div class="modal-header" style="justify-content: center">
          <h5 class="modal-title">Register</h5>
        </div>
        <div class="modal-body">
          <input
            type="text"
            class="form-control"
            placeholder="Username"
            aria-label="Username"
            aria-describedby="basic-addon1"
            style="margin-bottom: 10px"
          />
          <input
            type="password"
            class="form-control"
            placeholder="Password"
            aria-label="Password"
            aria-describedby="basic-addon2"
            style="margin-bottom: 10px"
          />
          <input
            type="password"
            class="form-control"
            placeholder="Confirm Password"
            aria-label="Confirm Password"
            aria-describedby="basic-addon3"
            style="margin-bottom: 10px"
          />
          <div style="text-align: center">
            <a href="#">Login</a>
          </div>
        </div>

        <div class="modal-footer" style="justify-content: center">
          <button type="button" class="btn btn-primary">Register</button>
        </div>
      </div>
    </div>
  </div>
</template>
