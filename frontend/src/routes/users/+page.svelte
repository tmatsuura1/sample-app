<script lang="ts">
  import { onMount } from 'svelte';
  const apiUrl = import.meta.env.VITE_BACKEND_API_URL;

  type User = {
    id: number;
    name: string;
  };
  let users: User[] = [];
  let searchId = '';
  let searchName = '';
  let sortOrder = 'asc'; // 'asc' or 'desc'
  let currentPage = 1;
  let usersPerPage = 5; // 1ページあたりの表示数
  let totalUsers = 0; // 全ユーザー数

  // ユーザーを取得する関数
  async function fetchUsers() {
    try {
      // 検索条件に基づいて総ユーザー数を取得
      const totalUsersResponse = await fetch(`${apiUrl}/users?${searchId ? `id=${searchId}&` : ''}${searchName ? `name=${searchName}&` : ''}`);
      if (totalUsersResponse.ok) {
        const allUsers = await totalUsersResponse.json();
        totalUsers = allUsers.length;
      } else {
        console.error('Failed to fetch total users:', totalUsersResponse.status);
      }

      // 検索条件とページネーションに基づいてユーザーを取得
      const queryParams = new URLSearchParams();
      queryParams.append('_page', currentPage.toString());
      queryParams.append('_limit', usersPerPage.toString());

      if (searchId) {
        queryParams.append('id', searchId);
      }
      if (searchName) {
        queryParams.append('name', searchName);
      }

      const response = await fetch(`${apiUrl}/users?${queryParams.toString()}`);
      if (response.ok) {
        users = await response.json();
        users = [...users]; // 配列の再代入でSvelteに変更を通知
        sortUsers();
      } else {
        console.error('Failed to fetch users:', response.status);
      }
    } catch (error) {
      console.error('Error fetching users:', error);
    }
  }

  // ソート機能
  function sortUsers() {
    users = [...users].sort((a, b) => {
      if (sortOrder === 'asc') {
        return a.name.localeCompare(b.name);
      } else {
        return b.name.localeCompare(a.name);
      }
    });
    users = [...users]; // 配列の再代入でSvelteに変更を通知
  }

  // ソート順を切り替える関数
  function toggleSortOrder() {
    sortOrder = sortOrder === 'asc' ? 'desc' : 'asc';
    sortUsers();
  }

  // ページを変更する関数
  function changePage(page:number) {
    const maxPage = Math.ceil(totalUsers / usersPerPage);
    if (page >= 1 && page <= maxPage) {
      currentPage = page;
      fetchUsers();
    }
  }

  onMount(fetchUsers); // コンポーネントがマウントされた時にユーザーリストを取得
</script>

<p>
  <a href="/register">Register New User</a>
</p>

<h1>User List</h1>
<input type="text" bind:value={searchId} placeholder="Search by ID" />
<input type="text" bind:value={searchName} placeholder="Search by Name" />
<button on:click={fetchUsers}>Search</button>

<button on:click={toggleSortOrder}>
  Sort by Name ({sortOrder === 'asc' ? 'Ascending' : 'Descending'})
</button>

<ul>
  {#each users as user}
    <li>
      <a href={`/detail/${user.id}`}>{user.id}: {user.name}</a>
    </li>
  {/each}
</ul>

<div>
  <button on:click={() => changePage(currentPage - 1)} disabled={currentPage === 1}>
    Previous
  </button>

  {#if totalUsers > 0}
    {#each Array(Math.ceil(totalUsers / usersPerPage)).fill() as _, i}
      <button on:click={() => changePage(i + 1)} class={currentPage === i + 1 ? 'active' : ''}>
        {i + 1}
      </button>
    {/each}
  {/if}

  <button on:click={() => changePage(currentPage + 1)} disabled={currentPage === Math.ceil(totalUsers / usersPerPage)}>
    Next
  </button>
</div>

<style>
  button.active {
    font-weight: bold;
  }
</style>
